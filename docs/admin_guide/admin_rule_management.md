# Admin Rule Management

This page explains how to create and manage administrative rules in GeoServer ACL. Admin rules control who can administer GeoServer resources.

## Understanding Admin Rules

Admin rules determine which users or roles have administrative access to which workspaces in GeoServer. Unlike data access rules, admin rules focus on controlling administrative privileges, not data access.

### Key Concepts

- **Admin Rules**: Define who can administer what in GeoServer
- **Workspace Administration**: Delegation of administrative tasks for specific workspaces
- **Rule Priority**: Determines which rule takes precedence when multiple rules match

### Admin Rule Components

Each admin rule consists of:

- **Priority**: Numerical priority (lower numbers = higher priority)
- **Username/Role**: The user or role the rule applies to
- **Workspace**: The workspace the rule applies to
- **Access Type**: The level of access granted (ADMIN, USER, or GROUP)

## Types of Administrative Access

GeoServer ACL supports different levels of administrative access:

### Admin Access Types

- **ADMIN**: Full administrative access (can administer all aspects of the specified workspaces)
- **USER**: User-level access (typically read-only access to administrative functions)
- **GROUP**: Group administration (can manage users within a specific group)

## Creating Admin Rules

You can create admin rules through the GeoServer ACL Admin UI or the REST API.

### Using the Admin UI

1. Log in to the GeoServer Admin UI
2. Navigate to "Security" → "ACL Admin Rules"
3. Click "Add new admin rule"
4. Fill in the rule parameters:
   - **Priority**: Numerical priority (lower = higher)
   - **Username/Role**: Specify a user or role
   - **Workspace**: Select a workspace or use * for all
   - **Access**: Select the access type
5. Click "Save"

### Using the REST API

To create an admin rule via the REST API:

```bash
curl -X POST \
  http://localhost:8080/geoserver-acl/api/adminrules \
  -H 'Content-Type: application/json' \
  -d '{
    "priority": 100,
    "username": "workspace_admin",
    "workspace": "citydata",
    "access": "ADMIN"
  }'
```

## Admin Rule Examples

### Global Administrator

This rule grants a user full administrative access to all workspaces:

```json
{
  "priority": 10,
  "username": "admin",
  "workspace": "*",
  "access": "ADMIN"
}
```

### Workspace Administrator

This rule allows a user to administer a specific workspace:

```json
{
  "priority": 100,
  "username": "citydata_admin",
  "workspace": "citydata",
  "access": "ADMIN"
}
```

### Role-Based Workspace Administration

This rule grants members of a specific role administration rights to a workspace:

```json
{
  "priority": 100,
  "rolename": "ROLE_WORKSPACE_ADMIN",
  "workspace": "citydata",
  "access": "ADMIN"
}
```

### Read-Only Access to Admin Interface

This rule grants user-level access without full administrative privileges:

```json
{
  "priority": 200,
  "rolename": "ROLE_VIEWER",
  "workspace": "public",
  "access": "USER"
}
```

## Admin Rule Prioritization

Admin rules are evaluated in priority order, with lower numbers having higher priority. When multiple rules match, the one with the highest priority (lowest number) takes effect.

### Best Practices for Prioritization

1. **Global administrators**: Assign the lowest numbers (10-50)
2. **Workspace administrators**: Use intermediate numbers (100-500)
3. **Limited access roles**: Use higher numbers (1000+)

Example priority scheme:

- Global admin: Priority 10
- Department admin: Priority 100
- Project admin: Priority 500
- Viewer roles: Priority 1000

## Managing Admin Rules

### Editing Rules

To edit an existing admin rule:

1. In the Admin UI, locate the rule in the list
2. Click "Edit"
3. Modify the parameters as needed
4. Click "Save"

Via REST API:

```bash
curl -X PUT \
  http://localhost:8080/geoserver-acl/api/adminrules/1 \
  -H 'Content-Type: application/json' \
  -d '{
    "priority": 100,
    "username": "workspace_admin",
    "workspace": "citydata",
    "access": "ADMIN"
  }'
```

### Deleting Rules

To delete an admin rule:

1. In the Admin UI, locate the rule in the list
2. Click "Delete"
3. Confirm the deletion

Via REST API:

```bash
curl -X DELETE http://localhost:8080/geoserver-acl/api/adminrules/1
```

### Reordering Rules

To change a rule's priority:

1. In the Admin UI, use the "Move Up/Down" buttons
2. Alternatively, edit the rule and assign a new priority value

## Testing Admin Rules

To test if your admin rules are working correctly:

1. Create a test user with the appropriate roles
2. Log in as that user
3. Verify that the user can access only the allowed workspaces in the admin interface
4. Test specific administrative actions to confirm permissions

## Implementation Strategies

### Hierarchical Administration

A common approach is to implement a hierarchical administration model:

1. **Global Administrators**: Full access to all workspaces and settings
2. **Department Administrators**: Manage multiple related workspaces
3. **Workspace Administrators**: Manage specific workspaces
4. **Publishers**: Able to add and edit data in specific workspaces

### Role-Based Administration

Another approach is to define administration by role:

1. Create distinct roles for different administrative functions
2. Assign users to appropriate roles
3. Create admin rules based on roles, not users
4. This simplifies user management and allows for rotation of responsibilities

Example roles:
- ROLE_GS_ADMIN: Global administrator
- ROLE_WS_ADMIN_CITYDATA: Workspace administrator for the "citydata" workspace
- ROLE_WS_PUBLISHER_CITYDATA: Publisher for the "citydata" workspace
- ROLE_WS_VIEWER_CITYDATA: Viewer for the "citydata" workspace

## Best Practices

### Security Principles

1. **Least Privilege**: Grant only the minimum necessary access
2. **Separation of Duties**: Divide administrative responsibilities
3. **Role-Based Access**: Prefer role-based rules over user-based rules
4. **Regular Audits**: Periodically review and clean up admin rules

### Rule Organization

1. **Consistent Naming**: Use a consistent pattern for usernames, role names, and workspaces
2. **Documentation**: Document the purpose of each rule
3. **Regular Cleanup**: Remove rules for users who no longer need access
4. **Rule Testing**: Verify rule behavior after creation or modification

## Troubleshooting

### Common Admin Rule Issues

1. **Rule Conflict**: Higher priority rule overrides intended access
   - Solution: Check rule priorities and ensure specific rules have higher priority than general rules

2. **Missing Access**: User doesn't have expected admin access
   - Solution: Verify that a matching rule exists and has the correct workspace and access type

3. **Unexpected Access**: User has more access than intended
   - Solution: Look for wildcard rules or role-based rules that may be providing unintended access

### Debugging Admin Rules

To diagnose admin rule issues:

1. List all admin rules and check their priorities
2. Verify the user's assigned roles
3. Check for wildcard patterns that might be matching unexpectedly
4. Enable debug logging for more detailed information

## Admin Rules vs. Data Rules

It's important to understand the difference between admin rules and data access rules:

- **Admin Rules**: Control administrative access to GeoServer (managing workspaces, layers, etc.)
- **Data Rules**: Control access to view, modify, or download data

A user might have administrative access to a workspace but still need data access rules to view or edit the data within that workspace when accessing it through OGC services.

## Integration with Authentication Systems

Admin rules work with GeoServer's authentication system:

1. Users authenticate through GeoServer's authentication mechanism
2. GeoServer ACL evaluates admin rules based on the authenticated user and roles
3. Administrative access is granted or denied based on matching rules

This allows integration with various authentication providers:
- LDAP/Active Directory
- OAuth2/OpenID Connect
- Database-based authentication
- Custom authentication providers