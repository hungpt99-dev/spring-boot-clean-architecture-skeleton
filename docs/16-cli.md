# CLI (Codegen) — NestJS-like scaffolding

Lightweight CLI to scaffold common pieces (similar spirit to NestJS CLI).

## Run

```bash
./gradlew :tooling:runCli --args="generate usecase Order --package=com.acme.shop"
./gradlew :tooling:runCli --args="generate controller Order --package=com.acme.shop"
./gradlew :tooling:runCli --args="generate dto Order --package=com.acme.shop --lang=en"
./gradlew :tooling:runCli --args="generate migration add_orders"
./gradlew :tooling:runCli --args="rename project cool-app --app-name=coolapp --config-prefix=coolapp --env-prefix=COOLAPP --group=com.acme"
./gradlew :tooling:runCli --args="list"
./gradlew :tooling:runCli --args="destroy <path>"
```

## Commands

- `generate usecase <Name>`  
  Creates `usecase/.../usecase/interactor/<Name>Interactor.java` with `@UseCaseComponent` + `@UseCaseTransactional` stub.

- `generate controller <Name>`  
  Creates `adapter/.../adapter/web/<Name>Controller.java` with `@ApiController` and a sample GET endpoint at `/api/v1/<name-kebab>`.
- `generate dto <Name>`  
  Creates `adapter/.../adapter/web/dto/<Name>Request.java`.
- `generate adapter <Name>`  
  Creates `adapter/.../adapter/persistence/<Name>Adapter.java` with `@AdapterComponent`.
- `generate port <Name>`  
  Creates `usecase/.../usecase/port/<Name>Port.java`.
- `generate test <Name>`  
  Creates `usecase/.../usecase/interactor/<Name>InteractorTest.java` (JUnit stub).
- `generate migration <name>`  
  Creates `application/src/main/resources/db/migration/V<timestamp>__<name>.sql`.
- `generate config <Name>`  
  Creates `application/.../config/<Name>Config.java` with `@Configuration`.
- `generate message`  
  Creates `application/src/main/resources/messages_<lang>.properties`.
- `rename project <NewName>`  
  Updates root project name (settings.gradle), `spring.application.name`, config prefix (`yourapp` → new prefix), env var prefix (`YOURAPP_` → new), and root `group`.
- `list`  
  Shows tracked generated files.
- `destroy <path>`  
  Removes a generated file and updates the manifest.

## Notes

- Idempotent: skips files that already exist.
- Uses simple string templates; adjust generated classes as needed.
- Runs from repo root (paths are relative to the multi-module layout).
- Base package can be provided via `--package=`, env `APP_BASE_PACKAGE`, or `.codegen/config.properties` (`basePackage`).
- Rename supports optional `--app-name`, `--config-prefix`, `--env-prefix`, `--group`; defaults derive from the new project name.
- Flags: `--force`, `--dry-run`, `--author`, `--tag`, `--lang`.
- You can supply `--config=path` to point to a custom config file (default: `.codegen/config.properties`).

