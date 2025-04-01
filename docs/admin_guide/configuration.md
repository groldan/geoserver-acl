# Configuration

This page explains how to configure GeoServer ACL after installation.

## Basic Configuration

GeoServer ACL configuration consists of two parts:

1. **ACL Service Configuration**: Settings for the standalone ACL service
2. **GeoServer Plugin Configuration**: Settings in GeoServer to connect to the ACL service

## ACL Service Configuration

The ACL service is configured through the `application.yml` file or environment variables.

### Core Settings

```yaml
geoserver:
  acl:
    # Basic service configuration
    baseUrl: http://localhost:8080/acl
    instanceName: default
    
    # Cache configuration
    cache:
      enabled: true
      expiration: 1h
      max-size: 10000
      
    # Default access behavior
    defaultDeny: true
```

### Database Configuration

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/acldb
    username: acl
    password: secret
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      idle-timeout: 30000
  
  jpa:
    database-platform: org.hibernate.spatial.dialect.postgis.PostgisDialect
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        jdbc.lob.non_contextual_creation: true
```

### Security Configuration

```yaml
geoserver:
  acl:
    security:
      # Enable/disable security
      enabled: true
      
      # Role required for full administrative access
      admin-role: ADMIN
      
      # Internal authentication (for standalone mode)
      internal:
        enabled: true
        
      # Pre-authentication (for integration with external auth)
      preauth:
        enabled: false
        user-header: X-Forwarded-User
        roles-header: X-Forwarded-Roles
```

### Web Server Configuration

```yaml
server:
  port: 8080
  servlet:
    context-path: /acl
  tomcat:
    max-threads: 200
    min-spare-threads: 10
    max-connections: 8192
```

### Monitoring and Management

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  health:
    db:
      enabled: true
  metrics:
    export:
      prometheus:
        enabled: true
```

## GeoServer Plugin Configuration

To configure the GeoServer plugin:

1. Log in to the GeoServer Admin interface
2. Navigate to "Security" → "GeoServer ACL"
3. Configure the plugin settings

### Basic Plugin Settings

- **ACL Services URL**: URL of the ACL service (e.g., `http://localhost:8080/acl`)
- **Instance Name**: Optional instance identifier (default: "default")
- **Allow Remote Services**: Whether to allow connections to remote ACL services
- **Default Allow**: Fall-back behavior when no rule matches (usually false)

### Cache Settings

- **Cache Enabled**: Whether to enable caching of authorization results
- **Cache Size**: Maximum number of entries in the cache
- **Cache Refresh Interval**: How often to refresh the cache

### Connection Settings

- **Connection Timeout**: Timeout for connecting to the ACL service
- **Read Timeout**: Timeout for reading responses from the ACL service
- **Max Connections**: Maximum number of connections in the pool

## Configuration Methods

GeoServer ACL can be configured through several methods:

### Configuration Files

The primary configuration method is through `application.yml` or `application.properties` files:

```bash
java -jar geoserver-acl-app.jar --spring.config.location=file:./application.yml
```

### Environment Variables

All configuration properties can be set through environment variables:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/acldb
export SPRING_DATASOURCE_USERNAME=acl
export SPRING_DATASOURCE_PASSWORD=secret
export GEOSERVER_ACL_SECURITY_ADMIN_ROLE=ADMIN
```

In Docker environments:

```yaml
environment:
  - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/acldb
  - SPRING_DATASOURCE_USERNAME=acl
  - SPRING_DATASOURCE_PASSWORD=secret
  - GEOSERVER_ACL_SECURITY_ADMIN_ROLE=ADMIN
```

### Command-Line Arguments

You can also specify configuration through command-line arguments:

```bash
java -jar geoserver-acl-app.jar --spring.datasource.url=jdbc:postgresql://localhost:5432/acldb
```

## Advanced Configuration

### Authentication Integration

GeoServer ACL can integrate with various authentication systems:

#### Basic Authentication

```yaml
geoserver:
  acl:
    security:
      internal:
        enabled: true
        # Users are defined in the database
```

#### Header-Based Authentication

```yaml
geoserver:
  acl:
    security:
      preauth:
        enabled: true
        user-header: X-Forwarded-User
        roles-header: X-Forwarded-Roles
```

#### OAuth2/OpenID Connect

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          keycloak:
            client-id: acl-service
            client-secret: your-client-secret
            scope: openid,email,profile
        provider:
          keycloak:
            issuer-uri: https://auth.example.com/realms/yourrealm
```

### Database Migration

GeoServer ACL uses Flyway for database migrations:

```yaml
spring:
  flyway:
    enabled: true
    baseline-on-migrate: true
    locations: classpath:db/migration/postgresql
```

### Event Broadcasting

For multi-instance setups, configure event broadcasting:

```yaml
spring:
  cloud:
    bus:
      enabled: true
    stream:
      rabbit:
        binder:
          nodes: localhost:5672
```

## Configuration Properties Reference

### Core Properties

| Property | Description | Default |
|----------|-------------|---------|
| `geoserver.acl.baseUrl` | Base URL of the ACL service | `http://localhost:8080/acl` |
| `geoserver.acl.instanceName` | Instance identifier | `default` |
| `geoserver.acl.defaultDeny` | Default behavior when no rule matches | `true` |

### Security Properties

| Property | Description | Default |
|----------|-------------|---------|
| `geoserver.acl.security.enabled` | Enable security | `true` |
| `geoserver.acl.security.admin-role` | Admin role identifier | `ADMIN` |
| `geoserver.acl.security.internal.enabled` | Enable internal authentication | `true` |
| `geoserver.acl.security.preauth.enabled` | Enable pre-authentication | `false` |
| `geoserver.acl.security.preauth.user-header` | Header for user identity | `X-Forwarded-User` |
| `geoserver.acl.security.preauth.roles-header` | Header for user roles | `X-Forwarded-Roles` |

### Cache Properties

| Property | Description | Default |
|----------|-------------|---------|
| `geoserver.acl.cache.enabled` | Enable caching | `true` |
| `geoserver.acl.cache.expiration` | Cache expiration | `1h` |
| `geoserver.acl.cache.max-size` | Maximum cache entries | `10000` |

## Example Configurations

### Simple Development Setup

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/acldb
    username: acl
    password: acl

geoserver:
  acl:
    security:
      enabled: true
      admin-role: ADMIN
```

### Production Setup with External Authentication

```yaml
spring:
  datasource:
    url: jdbc:postgresql://db.example.com:5432/acldb
    username: ${ACL_DB_USER}
    password: ${ACL_DB_PASSWORD}
    hikari:
      maximum-pool-size: 20

server:
  port: 8080
  servlet:
    context-path: /acl
    
geoserver:
  acl:
    cache:
      enabled: true
      max-size: 50000
      expiration: 30m
    security:
      enabled: true
      admin-role: ADMIN
      preauth:
        enabled: true
        user-header: X-Forwarded-User
        roles-header: X-Forwarded-Roles
```

### High-Availability Cluster Setup

```yaml
spring:
  datasource:
    url: jdbc:postgresql://db-cluster.example.com:5432/acldb
    username: ${ACL_DB_USER}
    password: ${ACL_DB_PASSWORD}
    hikari:
      maximum-pool-size: 30
  
  cloud:
    bus:
      enabled: true
    stream:
      rabbit:
        binder:
          nodes: rabbitmq-cluster.example.com:5672

geoserver:
  acl:
    cache:
      enabled: true
      max-size: 50000
      expiration: 15m
```

## Troubleshooting Configuration

### Common Issues

1. **Database Connection Problems**:
   - Check database credentials
   - Verify network connectivity
   - Check that the PostgreSQL server is running

2. **Authentication Issues**:
   - Verify role assignments
   - Check header names if using pre-authentication
   - Check CORS settings if using browser clients

3. **Performance Problems**:
   - Review cache settings
   - Check database connection pool size
   - Monitor memory usage and adjust JVM settings

### Configuration Validation

To validate your configuration:

1. Check application startup logs for warnings or errors
2. Use the health endpoints to verify component status:
   ```
   curl http://localhost:8080/acl/actuator/health
   ```
3. Test the ACL service connection from the GeoServer plugin