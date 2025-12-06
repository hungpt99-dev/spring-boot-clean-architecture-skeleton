# Use a base image with Java 25
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradle.properties .

# Grant execution permissions to gradlew
RUN chmod +x gradlew

# Install dependencies (this step is cached unless dependencies change)
# We run a dummy build or task to download dependencies
RUN ./gradlew dependencies --no-daemon || return 0

# Copy module build files
COPY adapter/build.gradle.kts adapter/
COPY application/build.gradle.kts application/
COPY domain/build.gradle.kts domain/
COPY usecase/build.gradle.kts usecase/
COPY tooling/build.gradle.kts tooling/

# Copy source code
COPY adapter/src adapter/src
COPY application/src application/src
COPY domain/src domain/src
COPY usecase/src usecase/src
COPY tooling/src tooling/src

# Build the application
# Use bash explicitly if needed, but usually standard shell is fine.
# Ensure line endings are LF for gradlew if copied from Windows.
RUN sed -i 's/\r$//' gradlew
RUN ./gradlew clean :application:build -x test --no-daemon

# Extract layers
FROM eclipse-temurin:25-jre AS layers
WORKDIR /app
COPY --from=builder /app/application/build/libs/application-*-SNAPSHOT.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Runtime stage
FROM eclipse-temurin:25-jre

WORKDIR /app

# Copy extracted layers
COPY --from=layers /app/dependencies/ ./
COPY --from=layers /app/spring-boot-loader/ ./
COPY --from=layers /app/snapshot-dependencies/ ./
COPY --from=layers /app/application/ ./

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
