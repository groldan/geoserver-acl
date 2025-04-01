# Deployment View

This section describes the technical infrastructure used to run GeoServer ACL and how its software components are mapped to that infrastructure.

## Infrastructure Requirements

GeoServer ACL has the following infrastructure requirements:

- **PostgreSQL 15+ with PostGIS 3.4+**: Required for data storage and geospatial operations
- **Java 17 Runtime**: Required for running the ACL service and components
- **Docker Runtime**: Optional but recommended for containerized deployment
- **Nginx**: Optional, used for SSL termination and proxying in containerized deployments

## System Topology

The standard topology consists of the following components:

### Database Service

- PostgreSQL database with PostGIS extension
- Stores ACL rules, admin rules, and configuration
- Runs in its own container or as an external service
- Resource requirements:
  - 2GB RAM minimum
  - 4 CPU cores recommended
  - 10GB storage minimum

### ACL Service

- Spring Boot application exposing the ACL REST API
- Provides rule management and authorization services
- Resource requirements:
  - 2GB RAM minimum
  - 4 CPU cores recommended
  - JVM settings: `-XX:MaxRAMPercentage=80.0`

### Gateway Service (Optional)

- Spring Cloud Gateway for API routing
- Provides authentication and request routing
- Resource requirements:
  - 1GB RAM minimum
  - 2 CPU cores recommended

### Nginx (Optional)

- Web server for SSL termination and proxying
- Provides HTTPS support and request routing
- Resource requirements:
  - 512MB RAM minimum
  - 1 CPU core recommended

## Deployment Options

GeoServer ACL supports multiple deployment models:

### Docker Compose Deployment

The reference deployment uses Docker Compose with four services:

1. **acldb**: PostgreSQL with PostGIS
   - External port: 6432 (mapped to 5432 internally)
   - Persistent volume for data

2. **acl**: GeoServer ACL service
   - External ports: 8180 (API), 8181 (management)
   - Dependencies: acldb

3. **gateway**: API Gateway
   - External port: 9090
   - Dependencies: acl

4. **nginx**: Web server
   - External port: 443 (HTTPS)
   - Dependencies: gateway
   - Mounts certificates from host

Configuration is defined in `/compose/compose.yml`:

```yaml
services:
  acldb:
    image: postgis/postgis:15-3.4-alpine
    environment:
      - POSTGRES_USER=acl
      - POSTGRES_PASSWORD=acl
      - POSTGRES_DB=acl
    ports:
      - "6432:5432"
    volumes:
      - acldb_data:/var/lib/postgresql/data

  acl:
    image: geoserveracl/acl:latest
    depends_on:
      - acldb
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://acldb:5432/acl
      - SPRING_DATASOURCE_USERNAME=acl
      - SPRING_DATASOURCE_PASSWORD=acl
    ports:
      - "8180:8080"
      - "8181:8081"

  gateway:
    image: geoserveracl/gateway:latest
    depends_on:
      - acl
    environment:
      - ACL_URL=http://acl:8080
    ports:
      - "9090:8080"

  nginx:
    image: nginx:alpine
    depends_on:
      - gateway
    volumes:
      - ./nginx.conf:/etc/nginx/conf.d/default.conf
      - ./cert.pem:/etc/nginx/conf.d/cert.pem
      - ./key.pem:/etc/nginx/conf.d/key.pem
    ports:
      - "443:443"

volumes:
  acldb_data:
```

### Standalone Service Deployment

GeoServer ACL can be deployed as a standalone service:

- Run as a Spring Boot application
- Requires external PostgreSQL with PostGIS
- Configuration via application.yml or environment variables
- Exposes REST API endpoints directly

### GeoServer Plugin Deployment

GeoServer ACL can be deployed within GeoServer:

- ACL plugin JAR deployed in GeoServer's WEB-INF/lib directory
- Configured through GeoServer's web interface
- Can connect to local or remote ACL service

## Network Configuration

### Default Ports

- PostgreSQL: 5432 (internal), 6432 (external)
- ACL Service: 8080 (internal), 8180 (external)
- ACL Management: 8081 (internal), 8181 (external)
- Gateway: 8080 (internal), 9090 (external)
- HTTPS: 443

### Network Security

- Internal communication uses HTTP
- External access should use HTTPS
- Database should be restricted to internal network
- Management port should be restricted to administrators

## Installation Procedure

The recommended installation procedure uses Docker Compose:

1. Clone the repository
2. Create SSL certificates if needed
3. Adjust environment variables in compose.yml if necessary
4. Run `docker-compose up -d`
5. Access the API at https://localhost/acl/api

For GeoServer plugin installation:

1. Download the ACL plugin JAR
2. Place it in GeoServer's WEB-INF/lib directory
3. Restart GeoServer
4. Configure the plugin through GeoServer's web interface