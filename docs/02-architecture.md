# Architecture Overview

This codebase follows **Clean Architecture** in a **Hexagonal (Ports & Adapters)** style.

Core ideas:

- **Business rules at the center** (domain + use cases)
- **Frameworks at the edge** (web, persistence, messaging)
- **Dependencies always point inward**

## Layers

1. **Domain**
   - Entities, value objects, domain services, factories, domain events
   - Pure Java, no Spring/JPA/HTTP dependencies
2. **Use Case**
   - Application services / interactors
   - Input and output boundaries (ports)
   - Coordinates domain objects to perform a business action
3. **Adapter**
   - Implementations of input/output ports
   - Web controllers, presenters, persistence gateways, event publishers
4. **Application (Infrastructure / Bootstrap)**
   - Spring Boot entry point
   - Dependency injection configuration
   - Global configuration (profiles, app properties)

This structure can be used as:

- A **modular monolith** (single deployable, strong module boundaries)
- A **template** for microservices (each microservice can follow the same module pattern)


