plugins {
    id("java-library")
    id("io.spring.dependency-management")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:4.0.0-M1")
    }
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.40")
    annotationProcessor("org.projectlombok:lombok:1.18.40")
    testCompileOnly("org.projectlombok:lombok:1.18.40")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.40")

    implementation(project(":usecase"))
    implementation(project(":domain"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation(platform("software.amazon.awssdk:bom:2.34.6"))
    implementation("software.amazon.awssdk:s3")
}

