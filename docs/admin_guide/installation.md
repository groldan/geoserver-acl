# Installation

This page describes how to install and deploy GeoServer ACL in different environments.

## Prerequisites

Before installing GeoServer ACL, ensure you have:

- **Java**: Java 11+ (Java 17+ recommended for production)
- **Database**: PostgreSQL 12+ with PostGIS extension
- **GeoServer**: GeoServer 2.19+ for plugin integration
- **Optional**: Docker and Docker Compose for containerized deployment

## Deployment Options

GeoServer ACL can be deployed in several configurations:

1. **Standalone Deployment**: The ACL service runs independently, and one or more GeoServer instances connect to it
2. **Embedded Deployment**: The ACL service runs within GeoServer (simpler but less scalable)
3. **Containerized Deployment**: Using Docker for both the ACL service and GeoServer
4. **Cloud Deployment**: For production environments, using container orchestration

## Standalone Deployment

### 1. Database Setup

First, create a PostgreSQL database for GeoServer ACL:

```sql
CREATE USER acl WITH PASSWORD 'your_secure_password';
CREATE DATABASE acldb OWNER acl;
\c acldb
CREATE EXTENSION postgis;
```

### 2. ACL Service Installation

1. Download the latest GeoServer ACL service JAR from the [releases page](https://github.com/geoserver/geoserver-acl/releases)

2. Create an `application.yml` configuration file:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/acldb
    username: acl
    password: your_secure_password
    driver-class-name: org.postgresql.Driver
  jpa:
    database-platform: org.hibernate.spatial.dialect.postgis.PostgisDialect
    hibernate:
      ddl-auto: validate

server:
  port: 8080
  servlet:
    context-path: /acl

geoserver:
  acl:
    security:
      enabled: true
      admin-role: ADMIN
```

3. Launch the service:

```bash
java -jar geoserver-acl-app.jar --spring.config.location=file:./application.yml
```

### 3. GeoServer Plugin Installation

1. Download the GeoServer ACL plugin ZIP from the [releases page](https://github.com/geoserver/geoserver-acl/releases)

2. Extract the ZIP contents to your GeoServer's `WEB-INF/lib` directory

3. Restart GeoServer

4. Configure the plugin:
   - Log in to GeoServer's admin interface
   - Go to "Security" → "GeoServer ACL"
   - Set the ACL service URL (e.g., `http://localhost:8080/acl`)
   - Test the connection and save

## Docker Deployment

For containerized deployment, GeoServer ACL provides Docker Compose configurations:

### 1. Get the Compose Files

Clone the repository or download the compose directory:

```bash
git clone https://github.com/geoserver/geoserver-acl.git
cd geoserver-acl/compose
```

### 2. Configure Environment

Edit the `compose.yml` file or create a `.env` file to set environment variables:

```
POSTGRES_USER=acl
POSTGRES_PASSWORD=acl
POSTGRES_DB=acl
ACL_ADMIN_USER=admin
ACL_ADMIN_PASSWORD=geoserver
```

### 3. Launch the Stack

Start the containers:

```bash
docker-compose up -d
```

This will launch:
- PostgreSQL with PostGIS
- GeoServer ACL service
- GeoServer with the ACL plugin pre-installed

### 4. Access the Services

- GeoServer: `http://localhost:8080/geoserver`
- ACL Service: `http://localhost:8181/acl`

## Production Deployment Considerations

For production environments, additional considerations are necessary:

### Security

1. **Use HTTPS**: Configure SSL/TLS for all services
2. **Secure Passwords**: Use strong, unique passwords for all components
3. **Database Security**: Restrict database access and enable encryption
4. **Authentication**: Integrate with your organization's authentication system

### High Availability

1. **Database Replication**: Set up PostgreSQL replication
2. **Load Balancing**: Deploy multiple ACL service instances behind a load balancer
3. **Health Checks**: Implement monitoring and health checks
4. **Backup Strategy**: Regular database backups

### Performance

1. **Connection Pooling**: Configure appropriate connection pool sizes
2. **JVM Tuning**: Allocate sufficient memory and optimize GC settings
3. **Caching**: Enable and configure caching options

## Example Production Configuration

Here's an example production configuration for the ACL service:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://db.example.com:5432/acldb
    username: ${ACL_DB_USER}
    password: ${ACL_DB_PASSWORD}
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
  jpa:
    properties:
      hibernate:
        jdbc.lob.non_contextual_creation: true
        dialect: org.hibernate.spatial.dialect.postgis.PostgisDialect

server:
  port: 8080
  servlet:
    context-path: /acl
  tomcat:
    max-threads: 200
    accept-count: 100

geoserver:
  acl:
    security:
      enabled: true
      admin-role: ADMIN
      # Use JWT or other auth
      preauth:
        enabled: true
        user-header: X-Forwarded-User
        roles-header: X-Forwarded-Roles

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  health:
    db:
      enabled: true
```

## Upgrading

To upgrade GeoServer ACL:

1. **Backup Database**: Always backup your ACL database before upgrading
2. **Check Release Notes**: Review changes and migration steps
3. **Upgrade ACL Service**: Replace the JAR or update the Docker image
4. **Upgrade GeoServer Plugin**: Replace the plugin JARs in GeoServer's lib directory
5. **Verify Configuration**: Ensure configurations are compatible with the new version
6. **Test**: Verify that rules work as expected after the upgrade

## Troubleshooting Installation

### Common Issues

1. **Database Connection Failures**:
   - Verify database credentials
   - Check network connectivity
   - Ensure PostgreSQL is running

2. **Plugin Not Loading**:
   - Check GeoServer logs for error messages
   - Verify that all required JARs are in the lib directory
   - Ensure compatible versions of GeoServer and the ACL plugin

3. **Service Startup Failures**:
   - Check for port conflicts
   - Verify Java version compatibility
   - Review application logs for error details

### Getting Help

If you encounter installation issues:

1. Check the [GitHub issues](https://github.com/geoserver/geoserver-acl/issues) for known problems
2. Review the logs for error messages
3. Join the [GeoServer community](https://geoserver.org/comm/) for support

## Next Steps

After installation:

1. [Configure](configuration.md) your GeoServer ACL instance
2. Define [data access rules](rule_management.md)
3. Set up [admin rules](admin_rule_management.md) for workspace administration
4. Configure [monitoring](monitoring.md) for your deployment