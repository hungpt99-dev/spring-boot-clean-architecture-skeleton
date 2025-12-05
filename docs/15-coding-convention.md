# Coding Conventions

These conventions keep the codebase consistent and easier to review.

## Packaging Strategy

- **Package by layer**, not by feature:
  - `domain.model`, `usecase.interactor`, `adapter.web`, etc.
- Within each layer, group by concept (user, order, etc.) when it improves clarity.

## General Java Style

- Use meaningful, domain‑driven names.
- Prefer immutability where possible.
- Use constructor injection (no field injection).

## Lombok & Annotations

- **Lombok usage**:
  - Lombok 1.18.40 is enabled in `adapter` and `application` modules.
  - Keep `domain` and `usecase` Lombok-free to avoid processor issues in core logic.
  - If you hit processor issues on JDK 25, temporarily disable Lombok or compile those modules with JDK 21 toolchains.

## Dependency Injection

- Use **constructor injection** everywhere.
- No field injection (`@Autowired` on fields).
- Keep wiring centralized in the `application` module where possible.

## Nullability & Validation

- Validate inputs at the edges:
  - Web DTOs: Bean Validation annotations.
  - Domain: validate invariants in constructors/factories.
- Prefer value objects over passing raw primitives around.


