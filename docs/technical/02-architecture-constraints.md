# Architecture Constraints

This section describes the various constraints that influenced the design and implementation of the GeoServer ACL system. These constraints are divided into technical constraints, organizational constraints, and conventions.

## Technical Constraints

| Constraint | Description |
|------------|-------------|
| Java Compatibility | GeoServer ACL service requires Java 17 for building, while the GeoServer plugin components must maintain compatibility with Java 11 to match GeoServer's requirements. |
| GeoServer Integration | The system must integrate with GeoServer's security architecture through its ResourceAccessManager extension point, ensuring compatibility with current and future GeoServer versions. |
| Database Compatibility | The persistence layer must support PostgreSQL as the primary database, with potential for other databases through JPA compatibility. |
| REST API Standards | The REST API must follow OpenAPI 3.0 standards for documentation and client generation. |
| Container Compatibility | The system must function properly in both traditional and containerized environments, particularly Docker and Kubernetes. |
| Authentication Flexibility | The system must work with various authentication mechanisms that GeoServer supports, including Basic Authentication, OAuth2, and OpenID Connect. |
| Performance Requirements | Authorization decisions must be processed efficiently to minimize impact on GeoServer request handling, with support for caching optimizations. |
| Backwards Compatibility | While improving upon GeoFence, GeoServer ACL must maintain functional compatibility with GeoFence rules and concepts to facilitate migration. |

## Organizational Constraints

| Constraint | Description |
|------------|-------------|
| Open Source Requirements | GeoServer ACL must be fully open source, licensed under GPL 2.0 to match GeoServer's licensing. |
| Community Collaboration | Development must follow the GeoServer community processes and integration standards. |
| Documentation Requirements | Comprehensive documentation must be provided for users, administrators, and developers. |
| Testing Standards | Code must have adequate test coverage, including unit tests and integration tests. |
| Release Cycle Alignment | Release cycles should align with GeoServer releases to ensure compatibility testing. |

## Conventions

### Development Conventions

| Convention | Description |
|------------|-------------|
| Code Style | Google Java Style (AOSP variant) with 4-space indentation is used for code formatting. |
| Package Structure | The project follows a domain-driven design package structure, separating concerns by domain functionality. |
| Naming Conventions | CamelCase for classes, camelCase for methods/variables. Descriptive names that reflect domain concepts. |
| Imports Organization | Imports are organized in blocks (java, javax, org, com) without wildcard imports. |
| Error Handling | Optional is used for nullable returns, with meaningful exceptions and proper handling. |
| Domain Objects | Immutable objects with builder pattern are used for domain entities. |
| Interface vs Implementation | Interfaces are preferred over concrete implementations in signatures to promote loose coupling. |
| Testing Naming | Unit tests are named with *Test suffix, integration tests with *IT suffix. |

### Architectural Conventions

| Convention | Description |
|------------|-------------|
| Domain-Driven Design | The architecture follows domain-driven design principles with clear bounded contexts. |
| Hexagonal Architecture | The core domain logic is isolated from external concerns through ports and adapters. |
| Dependency Direction | Dependencies point inward, with the domain model independent of external concerns. |
| Service Layer Pattern | Services coordinate operations on domain objects and provide transactional boundaries. |
| Repository Pattern | Persistence abstraction is provided through repositories that handle data access. |
| Command Query Responsibility Segregation (CQRS) | The API separates command (write) operations from query (read) operations where appropriate. |
| Event-Based Communication | Changes to rules can be published as events for notification and cache invalidation. |

These constraints and conventions guide the development of GeoServer ACL, ensuring a consistent, maintainable, and extensible system that meets the needs of its users and integrates well with the GeoServer ecosystem.