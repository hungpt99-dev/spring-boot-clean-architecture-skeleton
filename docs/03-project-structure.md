# Project Structure

Root layout:

```text
root
 ├── domain          # Core business model (no Spring)
 ├── usecase         # Application services / interactors
 ├── adapter         # Web, persistence, event adapters
 ├── application     # Spring Boot app + configuration
 ├── docs            # Architecture and usage documentation
 ├── build.gradle.kts
 ├── settings.gradle.kts
 └── gradle/         # Gradle wrapper
```

## Module Details

### `domain`

```text
domain/src/main/java/com/yourorg/yourapp/domain
 ├── model/          # User, UserId, UserStatus, etc.
 ├── factory/        # UserFactory, DefaultUserFactory
 └── event/          # DomainEvent, UserRegisteredEvent, ...
```

- **Responsibility**: Business concepts and invariants.
- **Rule**: No Spring, JPA, HTTP, logging dependencies.

### `usecase`

```text
usecase/src/main/java/com/yourorg/yourapp/usecase
 ├── inputboundary/
 ├── outputboundary/
 ├── interactor/
 └── requestresponsemodel/
```

- **Responsibility**: Orchestrate domain behavior; define ports.
- **Rule**: Depends only on `domain`.

### `adapter`

```text
adapter/src/main/java/com/yourorg/yourapp/adapter
 ├── web/            # Controllers, web DTOs, presenters
 ├── persistence/    # JPA entities, repositories, gateways
 └── event/          # Spring event publisher & handlers
```

- **Responsibility**: Talk to the outside world (HTTP, DB, messaging).
- **Rule**: Depends on `usecase` and `domain`, not the other way around.

### `application`

```text
application/src/main/java/com/yourorg/yourapp/config
 ├── Application.java
 ├── AppProperties.java
 ├── AnnotationRegistrar.java   # auto-registers @DomainComponent, @UseCaseComponent (configurable base packages)
 ├── UseCaseTransactionalAspect.java
 ├── LocalizationConfig.java
 ├── TimeZoneConfig.java
 └── ...
```

- **Responsibility**: Spring Boot setup, bean wiring, configuration properties.
- **Rule**: No business logic; just composition and configuration.


