# GeoServer Access Control List (ACL)

![build](https://github.com/geoserver/geoserver-acl/actions/workflows/build.yaml/badge.svg)
![plugin](https://github.com/groldan/geoserver-acl/actions/workflows/build-plugin.yaml/badge.svg)

<div align="center">
  <img src="docs/assets/images/acl.svg" alt="GeoServer ACL Logo" width="220px" />
  <br>
  <strong>Advanced geospatial authorization for GeoServer</strong>
</div>

## What is GeoServer ACL?

GeoServer ACL is a powerful authorization system that brings fine-grained access control to [GeoServer](https://geoserver.org/), allowing you to:

- **Control access to geospatial data** based on user identity, roles, and request properties
- **Restrict data by geographic area** through spatial filtering
- **Filter sensitive attributes** from layers on a per-user basis
- **Centralize authorization** across multiple GeoServer instances
- **Integrate seamlessly** with existing authentication systems

Whether you're protecting sensitive infrastructure data, implementing data privacy regulations, or managing access across organizational boundaries, GeoServer ACL provides the tools to implement sophisticated authorization policies while maintaining performance.

## Key Features

- **Fine-Grained Rules**: Control access at workspace, layer, or attribute level
- **Spatial Filtering**: Limit data access to specific geographic areas
- **Attribute Filtering**: Hide sensitive fields from unauthorized users
- **OGC Service Control**: Manage access to specific OGC services (WMS, WFS, WCS, etc.)
- **Role-Based Policies**: Define permissions based on user roles and groups
- **Rule Priority System**: Create sophisticated rule hierarchies with override capabilities
- **REST API**: Manage rules programmatically through a comprehensive API
- **Web Interface**: User-friendly UI for rule management directly in GeoServer
- **High Performance**: Optimized for production environments with caching support
- **Flexible Deployment**: Run as a standalone service or embedded in GeoServer

## Use Cases

- **Telecommunications**: Restrict field technicians to data in their assigned territories
- **Government**: Share public data while protecting sensitive information
- **Utilities**: Protect critical infrastructure data while enabling operational access
- **Environmental Agencies**: Control access to sensitive ecological data
- **Urban Planning**: Manage data access across multiple departments and stakeholders

See our [case studies](docs/case_studies/index.md) for real-world implementation examples.

## Architecture

GeoServer ACL consists of two main components:

1. **ACL Service**: A standalone Spring Boot application that manages access rules and provides authorization decisions
2. **GeoServer Plugin**: Integrates with GeoServer to enforce access control on each request

![System Architecture](docs/assets/images/structurizr/structurizr-SystemContext.svg)

## Getting Started

### Prerequisites

- Java 11+ (Java 17 recommended)
- PostgreSQL with PostGIS (for production deployments)
- GeoServer 2.19+

### Installation

The quickest way to get started is with Docker:

```bash
docker-compose -f compose/compose.yml up
```

For detailed installation instructions, see our [Installation Guide](docs/admin_guide/installation.md).

## Documentation

- [User Guide](docs/user_guide/index.md) - For users managing access rules
- [Administrator Guide](docs/admin_guide/index.md) - For system administrators
- [Developer Guide](docs/developer_guide/index.md) - For developers integrating with or extending ACL
- [Technical Documentation](docs/technical/index.md) - In-depth architecture and design documentation
- [API Reference](docs/api/index.md) - REST API documentation

## Building from Source

### Requirements

- Java 17 JDK
- Maven 3.8+

```bash
./mvnw clean install
```

A [Makefile](Makefile) is also provided with useful targets:

- `make format`: Format source code and project configuration
- `make lint`: Validate formatting
- `make install`: Build and install artifacts
- `make test`: Run unit and integration tests
- `make build-image`: Build Docker image
- `make run`: Run a development instance

## Contributing

We welcome contributions! Please see our [Contributing Guidelines](CONTRIBUTING.md) before submitting pull requests.

## License

GeoServer ACL is licensed under the [GPL v2.0 License](LICENSE).

## Acknowledgements

GeoServer ACL is a fork of [GeoFence](https://github.com/geoserver/geofence), bringing modern architecture and enhanced features while maintaining compatibility with its rule-based approach.