# Build & CI/CD

This project is built with **Gradle (Kotlin DSL)** and is designed to fit easily into most CI/CD systems.

## Build Basics

Common commands:

```bash
# compile and run tests for all modules
./gradlew clean build

# run only domain/usecase tests
./gradlew :domain:test
./gradlew :usecase:test

# run the application
./gradlew :application:bootRun

# build container image (Paketo buildpacks)
./gradlew :application:bootBuildImage

# build native image (GraalVM required)
./gradlew :application:nativeCompile
```

## CI Recommendations

Typical pipeline stages:

1. **Checkout**
2. **JDK & Gradle setup**
   - Use Gradle wrapper (`./gradlew`) for consistent versions.
3. **Static checks**
   - `./gradlew check`
4. **Tests**
   - `./gradlew test`
5. **Build artifacts**
   - `./gradlew :application:bootJar`
   - `./gradlew :application:bootBuildImage`
6. **Optional native build**
   - `./gradlew :application:nativeCompile`
7. **Deploy**
   - Push Docker image or native binary to target environment.

## Multi-Module Considerations

- You can run module‑specific pipelines:
  - `:domain:test` for pure domain changes.
  - `:adapter:test` for infrastructure changes.
- Build caching and parallelism can significantly speed up CI runs.


