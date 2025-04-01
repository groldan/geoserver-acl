# Building from Source

This page provides instructions for building GeoServer ACL from source code. It covers requirements, build commands, and development environment setup.

## Prerequisites

Before building GeoServer ACL, ensure you have the following prerequisites:

- **Java**: JDK 17 or higher (required for building)
- **Maven**: Version 3.8.0 or higher
- **Git**: For source code management
- **Docker**: (Optional) For testing and container builds
- **PostgreSQL with PostGIS**: (Optional) For local development and testing

## Getting the Source Code

Clone the GeoServer ACL repository from GitHub:

```bash
git clone https://github.com/geoserver/geoserver-acl.git
cd geoserver-acl
```

## Project Structure

The project is organized as a multi-module Maven project:

```
geoserver-acl/
├── docs/                  # Documentation
├── pom.xml                # Parent POM
├── src/                   # Source code
│   ├── application/       # Application services
│   ├── artifacts/         # Deployable artifacts
│   ├── domain/            # Domain model
│   ├── integration/       # Integration components
│   ├── openapi/           # OpenAPI definitions
│   └── plugin/            # GeoServer plugin
├── examples/              # Example code
├── compose/               # Docker Compose files
└── Makefile               # Makefile for common operations
```

## Build Commands

### Basic Build

To build all modules:

```bash
./mvnw clean install
```

Or using the Makefile:

```bash
make install
```

### Build without Tests

To build without running tests:

```bash
./mvnw clean install -DskipTests
```

### Run Tests

To run all tests:

```bash
./mvnw verify
```

Or using the Makefile:

```bash
make test
```

### Build Specific Modules

To build specific modules:

```bash
./mvnw clean install -pl src/domain/rule-management
```

### Format Code

To format the code according to project standards:

```bash
./mvnw sortpom:sort fmt:format
```

Or using the Makefile:

```bash
make format
```

### Lint Code

To check code formatting:

```bash
./mvnw sortpom:verify fmt:check
```

Or using the Makefile:

```bash
make lint
```

### Build Docker Image

To build the Docker image:

```bash
./mvnw clean package -DskipTests -Pdocker
```

Or using the Makefile:

```bash
make build-image
```

## Development Environment

### IDE Setup

#### IntelliJ IDEA

1. Import the project as a Maven project
2. Make sure to use JDK 17 or higher
3. Enable annotation processing for Lombok
4. Install the Google Java Format plugin for code formatting

#### Eclipse

1. Import the project as a Maven project
2. Make sure to use JDK 17 or higher
3. Install the Lombok plugin
4. Install the google-java-format plugin

### Code Style

GeoServer ACL follows the Google Java Style Guide (AOSP variant) with 4-space indentation:

- Use spaces for indentation (4 spaces)
- Line length limit: 100 characters
- CamelCase for class names and camelCase for method/variable names
- Organize imports properly (no wildcard imports)

The project includes formatter configurations in the build tools.

## Database Setup for Development

For local development with PostgreSQL:

1. Install PostgreSQL with PostGIS:

   ```bash
   # On Debian/Ubuntu
   sudo apt-get install postgresql postgresql-contrib postgis
   
   # On macOS with Homebrew
   brew install postgresql postgis
   ```

2. Create a database for development:

   ```sql
   CREATE USER acl WITH PASSWORD 'acl';
   CREATE DATABASE acldb OWNER acl;
   \c acldb
   CREATE EXTENSION postgis;
   ```

3. Configure the application to use your database:

   Create an `application.yml` file:

   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/acldb
       username: acl
       password: acl
       driver-class-name: org.postgresql.Driver
   ```

## Docker Development Environment

For an easier setup, you can use Docker Compose:

```bash
cd compose
docker-compose up -d
```

This will start:
- PostgreSQL with PostGIS
- GeoServer ACL service
- GeoServer with the ACL plugin (optional)

## Building the GeoServer Plugin

The GeoServer plugin requires compatibility with GeoServer's Java version (Java 11):

```bash
./mvnw clean install -pl src/plugin -Pgs-plugin
```

This builds the plugin module with Java 11 compatibility.

## Creating a Release

To create a release build:

1. Update version numbers:
   ```bash
   ./mvnw versions:set -DnewVersion=X.Y.Z
   ```

2. Build the project:
   ```bash
   ./mvnw clean install
   ```

3. Build the GeoServer plugin:
   ```bash
   ./mvnw clean install -pl src/plugin -Pgs-plugin
   ```

4. Build the Docker image:
   ```bash
   ./mvnw clean package -Pdocker
   ```

## Troubleshooting Build Issues

### Java Version Issues

If you encounter Java version issues:

```
Error: A JNI error has occurred, please check your installation and try again
Exception in thread "main" java.lang.UnsupportedClassVersionError: org/apache/maven/wrapper/MavenWrapperMain has been compiled by a more recent version of the Java Runtime
```

Make sure you're using JDK 17 or higher:

```bash
java -version
```

### Maven Dependency Issues

For Maven dependency issues:

```
Could not resolve dependencies for project org.geoserver.acl:...
```

Try cleaning your Maven repository and rebuilding:

```bash
rm -rf ~/.m2/repository/org/geoserver/acl
./mvnw clean install
```

### Docker Build Issues

If Docker build fails:

```
Failed to execute goal com.spotify:dockerfile-maven-plugin:...
```

Make sure Docker is running and you have permission to access it:

```bash
docker ps
```

## Next Steps

After building from source, you can:

- [Learn about the architecture](architecture.md)
- [Integrate with the API](api_integration.md)
- [Develop GeoServer plugin extensions](plugin_development.md)
- [Contribute to the project](contributing.md)