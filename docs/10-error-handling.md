# Error Handling

Error handling is split by layer to keep responsibilities clear.

## Domain Errors

- Represent **business rule violations**.
- Typically modeled as:
  - Checked/unchecked exceptions, or
  - Validation results / error objects.
- Should **not** know about HTTP status codes or logging.

Examples:

- `EmailAlreadyUsedException`
- `InvalidUserStateException`

## Use Case Errors

- Use cases may:
  - Propagate domain errors.
  - Wrap domain errors into more generic application errors.
- **Do not** throw `ResponseStatusException` here; keep them framework‑agnostic.

## Adapter / Web Errors

- `GlobalExceptionHandler` maps exceptions to HTTP responses:
  - Validation errors → 400 with `ApiErrorResponse`
  - Resource already exists → 409
  - Generic server errors → 500

Example strategy:

- Map known domain/usecase exceptions to specific status codes.
- Map unknown exceptions to a generic 500 with a safe, non‑leaky message.

## Logging Errors

- Log at **adapter/application** layers, not in domain.
- Include:
  - Correlation/trace IDs
  - Key user identifiers (e.g., userId, email) where appropriate and safe.


