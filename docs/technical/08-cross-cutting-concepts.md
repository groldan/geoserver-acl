# Cross-cutting Concepts

This section describes the overall, principal regulations and solution ideas that are relevant in multiple parts of the system. These concepts apply across multiple building blocks and are foundational to the system architecture.

## Design Patterns

GeoServer ACL employs various design patterns throughout its codebase to ensure maintainability, testability, and separation of concerns.

### Domain-Driven Design

The codebase follows Domain-Driven Design principles:

- **Bounded Contexts**: Clear separation between domain areas (rules, admin rules, authorization)
- **Entities and Value Objects**: Domain objects with distinct identity vs. immutable value objects
- **Repositories**: Abstractions for persistence operations
- **Domain Services**: Encapsulation of business logic that doesn't naturally fit into entities
- **Aggregates**: Clusters of domain objects treated as a unit (e.g., Rule with LayerDetails)

### Repository Pattern

The repository pattern is used to abstract data access:

- Repository interfaces defined in domain layer (`RuleRepository`, `AdminRuleRepository`)
- Implementation details encapsulated in infrastructure layer
- Memory-based implementations for testing (`MemoryRuleRepository`, `MemoryAdminRuleRepository`)
- Production implementations using JPA

### Builder Pattern

Complex object construction is handled via builders:

- `AccessRequestBuilder` for constructing authorization requests
- `CatalogSecurityFilterBuilder` for creating security filters
- Domain objects typically have builder methods for immutable construction

### Adapter Pattern

The adapter pattern is used for integration with external systems:

- `AclClientAdaptor` for integrating with the ACL REST API
- `ACLResourceAccessManager` for integrating with GeoServer

### Factory Pattern

Factory methods are used for creating complex objects:

- Password encoder factories for authentication
- Filter creation for repository queries

### Immutability

Domain objects are designed to be immutable where possible:

- Constructor-based initialization
- No setters for core properties
- Use of builders for object creation
- Defensive copying of collections

## Security Concepts

### Authentication

GeoServer ACL supports multiple authentication strategies:

- **HTTP Basic Authentication**: Username/password authentication
- **Pre-authentication Header**: External authentication via HTTP headers (user-header, roles-header)
- **Role-based Access Control**: Authorization based on user roles
- **Password Encoding**: Support for multiple encoding schemes (bcrypt, noop)

Configuration options are available via `SecurityConfigProperties` class:

```java
public class SecurityConfigProperties {
    private boolean enabled = true;
    private String adminRole = "ADMIN";
    private String preAuthUserHeader = "X-Forwarded-User";
    private String preAuthRolesHeader = "X-Forwarded-Roles";
    // ...
}
```

### Authorization

The core authorization concepts include:

- **Rule-based Authorization**: Access decisions based on matched rules
- **Priority Order**: Rules evaluated in priority order (highest first)
- **Catalog Mode**: Different strategies for handling unauthorized access (HIDE, MIXED, CHALLENGE)
- **Spatial Filtering**: Authorization constraints based on geographic boundaries
- **Layer Attributes**: Fine-grained control over data attributes

The authorization flow is implemented in `AuthorizationServiceImpl`:

```java
public AccessInfo getAccessInfo(AccessRequest request) {
    RuleFilter filter = buildRuleFilter(request);
    Rule rule = findFirstMatchingRule(filter);
    
    if (rule == null) {
        return buildDenyAccess();
    }
    
    return buildAccessInfo(rule, request);
}
```

## Persistence Concepts

### Object-Relational Mapping

GeoServer ACL uses JPA/Hibernate for ORM:

- Entity classes defined in JPA module
- Mapping between domain objects and database tables
- Support for spatial data types via JTS and PostGIS
- Transactions managed by Spring

### Database Migration

Flyway is used for database schema migrations:

- Migration scripts in SQL format
- Versioned migrations allow for updates
- Executed automatically during application startup

Examples of migration files:
- `/src/artifacts/api/src/main/resources/db/migration/postgresql/V1_0__ddl.sql`
- `/src/artifacts/api/src/main/resources/db/migration/postgresql/V1_1__rename_constraints.sql`

### Connection Pooling

HikariCP provides connection pooling:

- Optimized for high-performance environments
- Configurable pool sizes and timeout settings
- Automatic connection management

### Caching

Caffeine is used for in-memory caching:

- Authorization results cached for performance
- Configurable cache sizes and expiration
- Cache eviction on rule changes

## Testing Concepts

### Test Categories

GeoServer ACL uses different test categories:

- **Unit Tests**: Testing isolated components (suffix `Test`)
- **Integration Tests**: Testing component interactions (suffix `IT`)
- **End-to-End Tests**: Testing complete flows (in `java-e2e` module)

### Test Utilities

Various test utilities are provided:

- `MemoryRuleRepository` for in-memory rule storage during tests
- `MemoryAdminRuleRepository` for in-memory admin rule storage
- `ACLTestUtils` for common testing operations

### Test Containers

TestContainers is used for integration testing with real databases:

- `GeoServerAclContainer` for running containerized tests
- PostgreSQL with PostGIS for realistic data storage tests

## Error Handling

### Exception Hierarchy

Custom exceptions are used for domain-specific errors:

- `RuleIdentifierConflictException` for rule conflicts
- `AdminRuleIdentifierConflictException` for admin rule conflicts

### REST API Error Responses

REST API errors are handled consistently:

- HTTP status codes reflect error type
- Error response bodies contain details
- Problem Details format for error descriptions

### Validation

Input validation is performed at multiple levels:

- Domain-level validation in domain objects
- API-level validation in controllers
- Database-level constraints

## Logging

### Logging Framework

SLF4J with Logback is used for logging:

- Configurable log levels
- Console and file appenders
- Format customization

### Log Levels

Different log levels are used for different purposes:

- ERROR: Application failures
- WARN: Potential issues
- INFO: Important application events
- DEBUG: Detailed information for troubleshooting
- TRACE: Very detailed diagnostic information

### Request Logging

HTTP requests can be logged for debugging:

- Enabled with `logging_debug_requests` profile
- Includes request headers, body, and response status
- Useful for API troubleshooting