# Clean Architecture Rules

These rules are **non‑negotiable** for this template. They keep the codebase understandable and maintainable as it grows.

## Dependency Direction

```text
domain   ←   usecase   ←   adapter   ←   application
```

- All dependencies **must point inward**.
- Outer layers know about inner layers, never the opposite.

## Module Rules

- **Domain**
  - MUST NOT depend on:
    - Spring Boot
    - Spring Data / JPA
    - Web / Servlet APIs
    - Logging frameworks
  - Contains only:
    - Entities, value objects
    - Domain services / factories
    - Domain events

- **Usecase**
  - MUST NOT depend on:
    - Adapter implementations
    - Framework annotations
  - May depend on:
    - `domain` module
  - Contains:
    - Application services (interactors)
    - Ports (input/output boundaries)
    - Request/response models

- **Adapter**
  - May depend on:
    - `usecase`
    - `domain`
    - Frameworks (Spring Web, Spring Data, AMQP, etc.)
  - Contains:
    - Web controllers
    - Persistence gateways, repositories
    - Presenters, event publishers/handlers

- **Application**
  - May depend on all other modules.
  - Contains:
    - Spring Boot main class
    - Configuration properties
    - Bean wiring

## Coding Rules

- No business logic in:
  - Controllers
  - JPA entities
  - Configuration classes
- All business decisions belong in:
  - Domain entities / value objects
  - Use case interactors


