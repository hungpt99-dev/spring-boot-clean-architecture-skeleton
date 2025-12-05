# Adapter Guidelines

Adapters are **bridges between the outside world and the core application**.

There are three main adapter types in this project:

- **Web adapter** (HTTP)
- **Persistence adapter** (database)
- **Event adapter** (domain events → messaging or internal handlers)

## Web Adapter

Location:

```text
adapter/web
 ├── dto/                      # Request/response DTOs for HTTP
 ├── UserRegisterController.java
 ├── UserPresenterImpl.java
 ├── GlobalExceptionHandler.java
 └── ApiErrorResponse.java
```

Rules:

- Controllers:
  - Only handle HTTP concerns: path, status codes, headers, validation.
  - Convert HTTP DTOs ↔ use case models.
  - Delegate business logic entirely to use case interfaces.
- Presenters:
  - Implement output boundaries from `usecase`.
  - Format data for web responses.
- Global exception handler:
  - Map exceptions → `ApiErrorResponse` + HTTP status.

## Persistence Adapter

Location:

```text
adapter/persistence
 ├── entity/                   # JPA entities + mappers
 └── repository/               # Spring Data JPA repos + DS gateways
```

Rules:

- JPA entities:
  - Model database tables, not domain invariants.
  - No business logic—use domain objects for that.
- Data mappers:
  - Convert between domain entities and JPA entities.
- Gateway implementations:
  - Implement `UserDsGateway` using Spring Data JPA.
  - Use transactions where needed.

## Event Adapter

Location:

```text
adapter/event
 ├── SpringDomainEventPublisher.java
 └── UserRegisteredEventHandler.java
```

Rules:

- Publisher:
  - Implements `DomainEventPublisher`.
  - Bridges domain events into Spring’s event system.
- Handlers:
  - React to domain events (log, call messaging systems, send emails, etc.).


