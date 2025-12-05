# Logging & Tracing

Logging and tracing help observe the system without polluting domain logic.

## Principles

- **No logging in `domain`**.
- Log at:
  - `adapter` layer (web, persistence, event handlers).
  - `application` layer (startup, configuration, integration points).
- Prefer **structured logs** where possible.

## Recommended Practices

- Include:
  - Correlation/trace ID (from headers or generated per request).
  - Key business identifiers (e.g., userId) when safe.
- Avoid:
  - Logging sensitive data (passwords, tokens, secrets).
  - Excessive logging inside hot paths.

## Tracing

When integrating with an APM/Tracing system (e.g., OpenTelemetry):

- Configure tracing in the `application` module.
- Use filters/interceptors at the web/messaging level (adapter).
- Do not leak tracing APIs into domain/usecase layers.

## Request Logging (Implemented)

- `RequestLoggingFilter` (adapter/web):
  - Adds MDC keys: `requestId`, `clientIp`, `method`, `path`.
  - Generates a requestId if `X-Request-Id` is absent.
  - Logs completion with status and duration.
- Consume MDC in your log pattern to include request context.


