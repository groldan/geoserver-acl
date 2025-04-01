# API Integration

This page explains how to integrate with the GeoServer ACL API in your applications. It covers API concepts, client libraries, authentication, and common integration patterns.

## API Overview

The GeoServer ACL API is a RESTful API that allows you to:

- Manage access rules and admin rules
- Check authorization for specific resources
- Get summaries of accessible resources
- Monitor system health and status

The API is defined using OpenAPI 3.0 (formerly known as Swagger) and follows REST principles.

## API Documentation

### OpenAPI Specification

The OpenAPI specification for the API is available at:

```
http://localhost:8080/acl/api-docs
```

This specification is machine-readable and can be used to generate client libraries.

### Swagger UI

For interactive documentation, use the Swagger UI:

```
http://localhost:8080/acl/swagger-ui.html
```

This provides a web-based interface to explore and test the API.

## Client Libraries

GeoServer ACL provides generated client libraries for several programming languages:

### Java Client

#### Maven Dependency

```xml
<dependency>
  <groupId>org.geoserver.acl</groupId>
  <artifactId>gs-acl-api-client</artifactId>
  <version>1.0.0</version>
</dependency>
```

#### Basic Usage

```java
import org.geoserver.acl.client.AclClient;
import org.geoserver.acl.client.AclClientAdaptor;
import org.geoserver.acl.client.model.Rule;
import org.geoserver.acl.client.model.RuleFilter;

public class AclIntegrationExample {
    public static void main(String[] args) {
        // Create client
        AclClient client = new AclClient("http://localhost:8080/acl");
        client.setBasicAuth("admin", "geoserver");
        
        // Create adaptor (higher-level API)
        AclClientAdaptor acl = new AclClientAdaptor(client);
        
        // Get all rules
        List<Rule> rules = acl.getRules();
        
        // Create a rule
        Rule rule = new Rule()
            .priority(100)
            .access(AccessType.ALLOW)
            .username("user1")
            .workspace("workspace1")
            .layer("layer1");
        
        Rule createdRule = acl.insert(rule);
        
        // Find rules by filter
        RuleFilter filter = new RuleFilter()
            .workspace("workspace1");
        
        List<Rule> filteredRules = acl.getRules(filter);
        
        // Check authorization
        AccessRequest request = new AccessRequest()
            .user("user1")
            .workspace("workspace1")
            .layer("layer1")
            .service("WMS")
            .request("GetMap");
        
        AccessInfo accessInfo = acl.getAccessInfo(request);
        System.out.println("Access granted: " + accessInfo.isGrant());
    }
}
```

### JavaScript Client

#### Installation

```bash
npm install geoserver-acl-client
```

#### Basic Usage

```javascript
import { AclApi, RulesApi } from 'geoserver-acl-client';

// Set up authentication
const config = {
  basePath: 'http://localhost:8080/acl',
  username: 'admin',
  password: 'geoserver'
};

// Create API instances
const aclApi = new AclApi(config);
const rulesApi = new RulesApi(config);

// Get all rules
rulesApi.getRules()
  .then(rules => {
    console.log('Rules:', rules);
  })
  .catch(error => {
    console.error('Error:', error);
  });

// Check authorization
const accessRequest = {
  user: 'user1',
  workspace: 'workspace1',
  layer: 'layer1',
  service: 'WMS',
  request: 'GetMap'
};

aclApi.getAccessInfo(accessRequest)
  .then(accessInfo => {
    console.log('Access granted:', accessInfo.grant);
  })
  .catch(error => {
    console.error('Error:', error);
  });
```

### Python Client

#### Installation

```bash
pip install geoserver-acl-client
```

#### Basic Usage

```python
from geoserver_acl_client import AclApi, RulesApi, Configuration
from geoserver_acl_client.models import Rule, AccessRequest

# Set up configuration with authentication
config = Configuration(
    host="http://localhost:8080/acl",
    username="admin",
    password="geoserver"
)

# Create API instances
rules_api = RulesApi(config)
acl_api = AclApi(config)

# Get all rules
rules = rules_api.get_rules()
print(f"Found {len(rules)} rules")

# Create a rule
rule = Rule(
    priority=100,
    access="ALLOW",
    username="user1",
    workspace="workspace1",
    layer="layer1"
)

created_rule = rules_api.insert_rule(rule)
print(f"Created rule with ID: {created_rule.id}")

# Check authorization
request = AccessRequest(
    user="user1",
    workspace="workspace1",
    layer="layer1",
    service="WMS",
    request="GetMap"
)

access_info = acl_api.get_access_info(request)
print(f"Access granted: {access_info.grant}")
```

## Custom API Integration

If you need to integrate with a language that doesn't have a generated client, you can use the OpenAPI specification to generate your own client or make direct HTTP requests.

### Example HTTP Requests

#### Authentication

```
Authorization: Basic <base64-encoded-credentials>
```

#### Get All Rules

```http
GET /acl/api/rules HTTP/1.1
Host: localhost:8080
Authorization: Basic YWRtaW46Z2Vvc2VydmVy
Accept: application/json
```

#### Create a Rule

```http
POST /acl/api/rules HTTP/1.1
Host: localhost:8080
Authorization: Basic YWRtaW46Z2Vvc2VydmVy
Content-Type: application/json
Accept: application/json

{
  "priority": 100,
  "access": "ALLOW",
  "username": "user1",
  "workspace": "workspace1",
  "layer": "layer1"
}
```

#### Check Authorization

```http
POST /acl/api/authorization HTTP/1.1
Host: localhost:8080
Authorization: Basic YWRtaW46Z2Vvc2VydmVy
Content-Type: application/json
Accept: application/json

{
  "user": "user1",
  "roles": ["ROLE_USER"],
  "workspace": "workspace1",
  "layer": "layer1",
  "service": "WMS",
  "request": "GetMap"
}
```

## Authentication

The API supports several authentication methods:

### Basic Authentication

Most straightforward approach using username and password:

```http
Authorization: Basic YWRtaW46Z2Vvc2VydmVy
```

### Token-Based Authentication

If configured, you can use a bearer token:

```http
Authorization: Bearer <your-token>
```

### OAuth2/OpenID Connect

For integration with identity providers:

```http
Authorization: Bearer <oauth-token>
```

## API Endpoints

### Rules Management

- `GET /api/rules`: List all rules
- `POST /api/rules`: Create a new rule
- `GET /api/rules/{id}`: Get a specific rule
- `PUT /api/rules/{id}`: Update a rule
- `DELETE /api/rules/{id}`: Delete a rule
- `POST /api/rules/search`: Search for rules
- `POST /api/rules/count`: Count rules
- `POST /api/rules/bulk`: Bulk import rules

### Admin Rules Management

- `GET /api/adminrules`: List all admin rules
- `POST /api/adminrules`: Create a new admin rule
- `GET /api/adminrules/{id}`: Get a specific admin rule
- `PUT /api/adminrules/{id}`: Update an admin rule
- `DELETE /api/adminrules/{id}`: Delete an admin rule
- `POST /api/adminrules/search`: Search for admin rules
- `POST /api/adminrules/count`: Count admin rules
- `POST /api/adminrules/bulk`: Bulk import admin rules

### Authorization

- `POST /api/authorization`: Check access for a specific request
- `POST /api/authorization/summary`: Get an access summary

### Monitoring

- `GET /actuator/health`: Check system health
- `GET /actuator/info`: Get system information
- `GET /actuator/metrics`: Get system metrics

## Common Integration Patterns

### Rule Management Application

For administrative interfaces:

1. List and filter rules
2. Create, update, and delete rules
3. Validate rules
4. Manage rule priorities

```java
// Example of rule management
AclClientAdaptor acl = new AclClientAdaptor(client);

// Get all rules
List<Rule> rules = acl.getRules();

// Create a rule
Rule rule = new Rule()
    .priority(100)
    .access(AccessType.ALLOW)
    .username("user1")
    .workspace("workspace1")
    .layer("layer1");

Rule createdRule = acl.insert(rule);

// Update a rule
createdRule.setAccess(AccessType.LIMIT);
acl.update(createdRule);

// Delete a rule
acl.delete(createdRule.getId());
```

### Authorization Integration

For integrating authorization checks:

1. Create an access request based on the user and resource
2. Call the authorization API
3. Apply the access decisions

```java
// Example of authorization integration
AclClientAdaptor acl = new AclClientAdaptor(client);

// Check access
AccessRequest request = new AccessRequest()
    .user("user1")
    .roles(Arrays.asList("ROLE_USER"))
    .workspace("workspace1")
    .layer("layer1")
    .service("WMS")
    .request("GetMap");

AccessInfo accessInfo = acl.getAccessInfo(request);

if (accessInfo.isGrant()) {
    // Allow access
    // Apply any limitations (spatial, attributes, etc.)
    if (accessInfo.getRuleLimits() != null) {
        // Apply spatial limits
        Geometry allowedArea = accessInfo.getRuleLimits().getAllowedArea();
        // ...
    }
    
    if (accessInfo.getLayerDetails() != null) {
        // Apply attribute filters
        // ...
    }
} else {
    // Deny access
}
```

### Batch Processing

For bulk operations:

1. Export current rules
2. Process and modify the rules
3. Import the updated rules

```java
// Example of batch processing
AclClientAdaptor acl = new AclClientAdaptor(client);

// Export all rules
List<Rule> rules = acl.getRules();

// Process rules (e.g., update all rules for a specific workspace)
rules.stream()
    .filter(r -> "oldWorkspace".equals(r.getWorkspace()))
    .forEach(r -> r.setWorkspace("newWorkspace"));

// Import updated rules
acl.setRules(rules);
```

## Error Handling

The API uses standard HTTP status codes:

- 200 OK: Successful operation
- 201 Created: Resource created
- 400 Bad Request: Invalid input
- 401 Unauthorized: Authentication failed
- 403 Forbidden: Permission denied
- 404 Not Found: Resource not found
- 409 Conflict: Resource conflict
- 500 Internal Server Error: Server error

Error responses include JSON details:

```json
{
  "timestamp": "2023-09-20T10:15:30.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid rule format",
  "path": "/acl/api/rules"
}
```

In your client code, handle these errors appropriately:

```java
try {
    Rule rule = acl.get("non-existent-id");
} catch (ApiException e) {
    if (e.getCode() == 404) {
        // Handle not found
    } else {
        // Handle other errors
    }
}
```

## Performance Considerations

### Caching

Consider caching repeated authorization checks to improve performance:

```java
// Simple in-memory cache
Map<AccessRequest, AccessInfo> cache = new ConcurrentHashMap<>();

public AccessInfo getAccessInfo(AccessRequest request) {
    return cache.computeIfAbsent(request, r -> acl.getAccessInfo(r));
}
```

### Batch Operations

Use batch operations for large updates:

```java
// Bulk create/update is more efficient than individual calls
acl.setRules(batchOfRules);
```

### Pagination

For large result sets, use pagination:

```java
// Get rules with pagination
RuleFilter filter = new RuleFilter()
    .workspace("workspace1");

// Get page 1 with 50 items per page
List<Rule> page1 = acl.getRules(filter, 0, 50);

// Get page 2
List<Rule> page2 = acl.getRules(filter, 50, 50);
```

## Security Best Practices

1. **Use HTTPS**: Always use HTTPS for API communication
2. **Secure Credentials**: Protect API credentials
3. **Minimize Permissions**: Use accounts with minimal required permissions
4. **Validate Inputs**: Validate all inputs before sending to the API
5. **Handle Errors Gracefully**: Properly handle and log errors
6. **Audit API Usage**: Log API calls for auditing

## Advanced Integration

### Event-Driven Integration

For systems that need to react to rule changes:

1. Use a message broker (e.g., RabbitMQ)
2. Configure GeoServer ACL to publish events
3. Subscribe to the events in your application

### Multi-Instance Synchronization

For distributed deployments:

1. Use a shared database
2. Configure event broadcasting between instances
3. Implement cache synchronization

## Troubleshooting Integration Issues

### Connection Issues

If you have trouble connecting to the API:

1. Verify the API URL is correct
2. Check network connectivity
3. Verify authentication credentials
4. Check for SSL certificate issues
5. Look for firewall or proxy restrictions

### API Response Issues

If the API returns unexpected results:

1. Check the request payload for errors
2. Look for validation errors in the response
3. Examine the logs on the server side
4. Try the same request in the Swagger UI

### Authorization Issues

If authorization checks don't work as expected:

1. Verify the rule definitions
2. Check rule priorities
3. Ensure the AccessRequest includes all necessary fields
4. Test with the built-in authorization testing tools