# Project Overview

This project is a **Spring Boot Clean Architecture skeleton** implemented as a **multi‑module Gradle build**.
It is intended to be a **base project / internal template** so teams can start new services with:

- **Clean Architecture / Hexagonal** boundaries
- **Domain‑Driven Design** focus
- **Java 25** and **Spring Boot 4.0.0‑M1**
- **GraalVM native image** readiness
- **Cloud / VPS friendly** configuration

## Goals

- **Maintainable**: Business rules are isolated from frameworks and infrastructure.
- **Testable**: Domain and use cases can be unit‑tested without Spring, HTTP, or a database.
- **Replaceable adapters**: Web, persistence, and messaging are pluggable.
- **Scalable structure**: Works as a **modular monolith** today and as a base for future microservices.

## Tech Stack (Current)

- **Language**: Java 25 (via Gradle toolchains)
- **Framework**: Spring Boot 4.0.0‑M1
- **Build**: Gradle 8.14+ (wrapper), Kotlin DSL, multi‑module
- **Persistence**: Spring Data JPA + PostgreSQL driver
- **Messaging**: Spring AMQP starter (placeholder for RabbitMQ)
- **Observability**: Spring Boot Actuator
- **Native**: GraalVM with `org.graalvm.buildtools.native`


