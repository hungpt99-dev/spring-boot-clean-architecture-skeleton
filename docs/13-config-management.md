# Configuration Management

Configuration is centralized in the **`application` module** to keep core code environment‑agnostic.

## Where Configuration Lives

- `application/src/main/resources/application.yml`
- `application/src/main/resources/application-dev.yml`
- `application/src/main/resources/application-staging.yml`
- `application/src/main/resources/application-prod.yml`
- `AppProperties` (`application/src/main/java/com/yourorg/yourapp/config/AppProperties.java`)

## Principles

- No config files in `domain`, `usecase`, or `adapter` modules.
- Use **Spring Profiles** (`dev`, `staging`, `prod`) to switch environments.
- Use **typed configuration properties** instead of scattered `@Value` injections.

## Typical Settings

- Datasource:
  - URL, username, password, pool settings.
- Messaging:
  - Broker URL, exchange names.
- Environment metadata:
  - Environment name, region, cloud platform.
- Management:
  - Actuator exposure, probes, health checks.

## Best Practices

- Local development:
  - Keep sensible defaults in `application.yml`.
  - Use `application-dev.yml` for dev‑only overrides.
- Higher environments:
  - Keep secrets out of source control.
  - Use environment variables or external config stores (Vault, Parameter Store).
- Adding new config:
  1. Add fields to `AppProperties`.
  2. Expose in `application.yml` with defaults if appropriate.
  3. Inject `AppProperties` into beans that need it in the `application` module.


