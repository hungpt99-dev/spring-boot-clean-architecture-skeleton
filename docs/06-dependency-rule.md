# Dependency Rules

Clean Architecture in this project is enforced both **conceptually** and **physically**.

## Conceptual Dependency Graph

```text
          +----------------------+
          |      Application     |
          +----------+-----------+
                     |
                     v
          +----------------------+
          |       Adapter        |
          +----------+-----------+
                     |
                     v
          +----------------------+
          |       Use Case       |
          +----------+-----------+
                     |
                     v
          +----------------------+
          |        Domain        |
          +----------------------+
```

## Allowed Dependencies

- `usecase` → `domain`
- `adapter` → `usecase` + `domain`
- `application` → `adapter` + `usecase` + `domain`

## Forbidden Dependencies

- `domain` → any Spring / JPA / HTTP / logging APIs
- `usecase` → adapter implementations (web, JPA, messaging)
- `adapter` → `application`

## How Gradle Enforces This

- `settings.gradle.kts` declares four modules:
  - `domain`, `usecase`, `adapter`, `application`
- `usecase/build.gradle.kts` only depends on `domain`.
- `adapter/build.gradle.kts` depends on `usecase` and `domain`.
- `application/build.gradle.kts` depends on all three.

Any attempt to import a class from an outer layer where it is not allowed will cause a **compile error**, not just a style violation.


