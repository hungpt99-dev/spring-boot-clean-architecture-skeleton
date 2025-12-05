# Use Case Guidelines

Use cases model **application-specific business actions** (e.g., register user, update profile, deactivate account).

## Principles

- **One Use Case = One Business Action**
  - Example: `UserRegisterInteractor` handles “register a new user”.
- **Interface‑first**
  - Define input boundary (interface) before implementation.
- **Framework‑free**
  - No Spring annotations, no HTTP, no JPA APIs.
  - Only plain Java, `domain` types, and `usecase` ports.
- **Registration**
  - Mark interactors with `@UseCaseComponent` (usecase module).
  - The application-layer registrar scans packages defined in `app.usecase.base-packages`.

## Structure

Typical package structure:

```text
usecase
 ├── inputboundary/        # Interfaces called by controllers/other adapters
 ├── outputboundary/       # Data store, presenter, other output ports
 ├── interactor/           # Implementations of input boundaries
 └── requestresponsemodel/ # Request and response models
```

## Example

```java
// inputboundary/UserInputBoundary.java
public interface UserInputBoundary {
    UserResponseModel registerUser(UserRequestModel request);
}

// interactor/UserRegisterInteractor.java
public class UserRegisterInteractor implements UserInputBoundary {
    private final UserFactory userFactory;
    private final UserDsGateway userGateway;
    private final UserPresenter presenter;
    private final DomainEventPublisher eventPublisher;

    // constructor injection...

    @Override
    public UserResponseModel registerUser(UserRequestModel request) {
        // business flow (no Spring, no HTTP, no JPA)
    }
}
```

## What NOT to do in Use Cases

- Do **not**:
  - Annotate with `@Service`, `@Transactional`, `@Component`.
  - Inject repositories directly from Spring Data—use `UserDsGateway` instead.
  - Read environment variables or configuration directly—those are for `application` and adapters.
  - Rely on component scanning defaults without setting `app.usecase.base-packages`; no beans will be registered otherwise.


