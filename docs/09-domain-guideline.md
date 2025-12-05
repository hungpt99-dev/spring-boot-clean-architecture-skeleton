# Domain Guidelines

The `domain` module is the **heart of the system**. It must remain independent from frameworks and infrastructure.

## Responsibilities

- Define **ubiquitous language**: `User`, `UserId`, `UserStatus`, etc.
- Express business invariants:
  - Valid state transitions
  - Rules around activation, locking, archiving, etc.
- Provide domain events:
  - `UserRegisteredEvent` and others when important business events occur.
- Provide factories:
  - `UserFactory` encapsulates how new users are created.

## Rules

- **No framework annotations** in domain classes:
  - No `@Entity`, `@Table`, `@Component`, `@Service`, etc.
- **No references** to:
  - Spring, JPA, Servlet API, HTTP, JSON, logging frameworks.
- **Only references** to:
  - Java standard library
  - Other domain types within the module

## Example

```java
public interface User {
    UserId id();
    String email();
    String displayName();
    UserStatus status();
}
```

```java
public enum UserStatus {
    PENDING_VERIFICATION,
    ACTIVE,
    LOCKED,
    ARCHIVED
}
```

## Testing the Domain

- Domain tests should be:
  - Pure JUnit tests, no Spring context.
  - Fast and deterministic.
- Focus on:
  - Value object behavior.
  - Invariants (e.g., cannot activate a locked user without specific actions).
  - Event emission from domain operations, if applicable.


