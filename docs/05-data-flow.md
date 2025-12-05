# Data Flow

This document explains **how data moves through the system** using the User Registration use case as an example.

## High-Level Flow

```text
Client
  ↓
Controller (adapter/web)
  ↓
Use Case Interactor (usecase)
  ↓
Domain Model (domain)
  ↓
Repository / Gateway (adapter/persistence)
  ↓
Presenter (usecase + adapter/web)
  ↓
HTTP Response
```

## User Registration Flow

1. **HTTP Request**
   - Endpoint: `POST /api/v1/users`
   - Body mapped to `UserRegisterRequest` (adapter/web).

2. **Validation**
   - Bean Validation annotations (`@Email`, `@NotBlank`, etc.) validate incoming data.
   - Invalid requests are handled by `GlobalExceptionHandler`.

3. **Use Case Invocation**
   - Controller converts `UserRegisterRequest` → `UserRequestModel`.
   - Calls `UserInputBoundary.registerUser(UserRequestModel)` (usecase).

4. **Business Logic Execution**
   - `UserRegisterInteractor`:
     - Checks if email already exists via `UserDsGateway`.
     - Builds domain command `UserRegistrationCommand`.
     - Uses `UserFactory` to create a `User`.

5. **Persistence**
   - Interactor calls `userGateway.save(user)`.
   - `SpringDataUserDsGateway`:
     - Maps domain `User` → `UserEntity` (JPA).
     - Uses `JpaUserRepository` to persist.
     - Maps back to domain `User`.

6. **Domain Event**
   - Interactor publishes `UserRegisteredEvent` via `DomainEventPublisher`.
   - `SpringDomainEventPublisher` wraps Spring’s `ApplicationEventPublisher`.
   - `UserRegisteredEventHandler` receives and processes the event (currently logs, later can push to AMQP/Kafka).

7. **Presentation**
   - Interactor delegates to `UserPresenter`:
     - Maps `User` → `UserResponseModel`.
   - Controller returns `UserResponseModel` as HTTP JSON with appropriate status code.


