# API Reference

GeoServer ACL provides a comprehensive REST API for managing rules and performing authorization checks. The API is specified using the OpenAPI 3.0 standard, allowing for easy integration and client generation.

## API Overview

The GeoServer ACL API consists of three main sections:

1. **Data Rules API**: Manage access rules for GeoServer resources
2. **Admin Rules API**: Manage administrative permissions for workspaces
3. **Authorization API**: Perform authorization checks against defined rules

## Using the API

### Authentication

The API supports various authentication methods:

- **Basic Authentication**: Username and password
- **OAuth2/OpenID Connect**: Bearer token authentication (when configured)
- **API Key**: API key authentication (when configured)

### Content Types

The API supports the following content types:

- **JSON**: `application/json` (default)
- **XML**: `application/xml` (when explicitly requested)

### Error Handling

The API follows standard HTTP status codes:

- **200 OK**: Successful operation
- **201 Created**: Resource successfully created
- **400 Bad Request**: Invalid input
- **401 Unauthorized**: Authentication failure
- **403 Forbidden**: Authorization failure
- **404 Not Found**: Resource not found
- **409 Conflict**: Resource conflict (e.g., duplicate rule)
- **500 Server Error**: Internal server error

Error responses include a standardized JSON structure:

```json
{
  "error": {
    "code": "INVALID_PARAMETER",
    "message": "Invalid value for parameter 'priority'"
  }
}
```

## API Documentation

Full API documentation is available:

- **Interactive Documentation**: Swagger UI at `/swagger-ui.html`
- **OpenAPI Specification**: Available at `/api-docs`

## Client Libraries

GeoServer ACL provides generated client libraries for easy integration:

- **Java Client**: Maven artifact `org.geoserver.acl:acl-client`
- **JavaScript Client**: NPM package `@geoserver/acl-client`
- **Python Client**: PyPI package `geoserver-acl-client`

You can also generate clients for other languages using the OpenAPI specification.

## Next Sections

- [Data Rules API](rules.md) - API for managing data access rules
- [Admin Rules API](admin_rules.md) - API for managing administrative rules
- [Authorization API](authorization.md) - API for authorization checks
- [Client Examples](client_examples.md) - Examples of using the client libraries