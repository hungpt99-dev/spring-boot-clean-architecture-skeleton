# Spring Boot Clean Architecture Skeleton

Opinionated clean architecture + DDD + hexagonal skeleton built with Spring Boot 4, Java 25, Gradle, and GraalVM native image readiness. Includes multi-locale support (messages, locale resolver) and default UTC time-zone handling with configurable overrides.

## Modules

| Module | Responsibility | Notes |
| --- | --- | --- |
| `domain` | Core entities, value objects, factories, domain events | No Spring dependency |
| `usecase` | Input/output boundaries, interactors | Depends only on `domain` |
| `adapter` | Web, persistence, event adapters | Depends on `usecase` + frameworks |
| `application` | Spring Boot entrypoint, configuration, environment wiring | Produces runnable jar/native image |

The `application` module composes everything at runtime, keeping architectural boundaries explicit.
See [`docs/project-overview.md`](docs/project-overview.md) for full architecture and build details.

## Getting Started

```bash
./gradlew bootRun
```

Profiles: `dev` (default), `staging`, `prod`. Override via `SPRING_PROFILES_ACTIVE`.

## Native Image

```bash
./gradlew nativeCompile
```

## Next Steps

- Wire real database credentials and run migrations before enabling prod profile.
- Replace placeholder messaging implementation with actual broker integration.
- Extend domain with richer aggregates and boundary-specific request/response models.

