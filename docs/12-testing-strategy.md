# Testing Strategy

The testing strategy mirrors the architecture layers.

## Domain Tests

- **Type**: Unit tests
- **Scope**: Entities, value objects, domain services, factories.
- **Stack**: JUnit only, no Spring.

Examples:

- `BasicUser` status transitions.
- `UserId` validation.

## Use Case Tests

- **Type**: Unit tests
- **Scope**: Interactors and ports (using mocks/stubs).
- **Stack**: JUnit + mocking framework (e.g., Mockito).

Examples:

- `UserRegisterInteractor`:
  - When email exists → calls presenter “already exists”.
  - When email new → creates user, saves via gateway, publishes event.

## Adapter Tests

- **Type**: Integration tests
- **Scope**:
  - Controllers (WebMvc/WebFlux tests)
  - Persistence gateways (JPA with test DB or containers)
- **Stack**: Spring Boot test slices, Testcontainers if desired.

Examples:

- `UserRegisterController`:
  - Validation errors → 400
  - Successful registration → 201 with correct JSON body.

## Application / E2E Tests

- **Type**: End‑to‑end
- **Scope**: Full app running (HTTP, DB, messaging, etc.).
- **Stack**: Spring Boot test context, possibly real or containerized dependencies.

Examples:

- Start app → hit `/actuator/health` → expect UP.
- Full registration flow against an ephemeral database.


