# Rule Management

This page explains how to create, manage, and organize data access rules in GeoServer ACL.

## Understanding Rules

Rules are the core mechanism for controlling access to GeoServer resources. Each rule specifies:

1. **Who** the rule applies to (users, roles, IP address)
2. **What** resources it affects (workspaces, layers, services)
3. **How** access is granted or restricted (allow, deny, limit)

### Rule Components

A typical rule includes:

- **Priority**: Determines the order in which rules are evaluated (lower numbers = higher priority)
- **User/Role**: The users or roles the rule applies to
- **Service**: The OGC service (WMS, WFS, etc.) or * for all services
- **Request**: The specific request type or * for all requests
- **Workspace**: The GeoServer workspace or * for all workspaces
- **Layer**: The specific layer or * for all layers
- **Access Type**: Allow, Deny, or Limit
- **Limits**: Additional restrictions (for Limit access type)

### Rule Resolution

When a request comes in, GeoServer ACL finds all matching rules and applies the one with the highest priority (lowest number). If no rules match, the default rule is applied.

## Creating Rules

You can create rules through:

1. The GeoServer ACL Admin UI (part of the GeoServer Plugin)
2. The GeoServer ACL REST API

### Using the Admin UI

1. Log in to the GeoServer Admin UI
2. Navigate to "Security" → "ACL Rules"
3. Click "Add new rule"
4. Fill in the rule parameters
5. Set the priority and position
6. Click "Save"

![Add Rule UI](../assets/images/add_rule_ui.png)

### Using the REST API

To create a rule via the REST API:

```bash
curl -X POST \
  http://localhost:8080/geoserver-acl/api/rules \
  -H 'Content-Type: application/json' \
  -d '{
    "priority": 100,
    "access": "ALLOW",
    "username": "analyst",
    "workspace": "citydata",
    "layer": "buildings"
  }'
```

## Rule Prioritization

Rules are evaluated in priority order. To change a rule's priority:

1. In the Admin UI, use the "Priority" field or the "Move Up/Down" buttons
2. Via REST API, update the rule with a new priority value

### Best Practices for Prioritization

1. **More specific rules should have higher priority**
2. **Deny rules generally should have higher priority than Allow rules**
3. **Start with a large priority gap between rules to make insertion easier**

Example priority scheme:

- Default rule: Priority 999999 (lowest priority)
- Global rules: Priority 100000-999998
- Workspace rules: Priority 10000-99999
- Layer rules: Priority 1000-9999
- User-specific rules: Priority 1-999

## Rule Examples

### Allow access to a specific layer

```json
{
  "priority": 100,
  "access": "ALLOW",
  "username": "*",
  "workspace": "public",
  "layer": "basemap"
}
```

### Deny WFS access to sensitive data

```json
{
  "priority": 50,
  "access": "DENY",
  "username": "*",
  "service": "WFS",
  "workspace": "sensitive",
  "layer": "*"
}
```

### Limit access to a geographic area

```json
{
  "priority": 75,
  "access": "LIMIT",
  "username": "fieldworker",
  "workspace": "projects",
  "layer": "survey_areas",
  "ruleLimits": {
    "allowedArea": "POLYGON((...))",
    "spatialFilterType": "INTERSECT"
  }
}
```

## Organizing Rules

Large rule sets can become complex. Here are some strategies for organization:

### Layering Approach

1. **Base Layer**: Default deny rule with lowest priority
2. **Service Layer**: Rules that apply to entire services
3. **Workspace Layer**: Rules that apply to workspaces
4. **Layer Layer**: Rules that apply to specific layers
5. **User/Role Layer**: Specific rules for users or roles

### Rule Groups

Consider grouping rules conceptually:

- Public access rules
- Department-specific rules
- Project-specific rules
- Administrative rules

## Testing Rules

To test if your rules are working correctly:

1. In the Admin UI, use the "Test Rules" feature
2. Provide test parameters (user, role, service, etc.)
3. See which rules would match and what access would be granted

Via REST API:

```bash
curl -X POST \
  http://localhost:8080/geoserver-acl/api/authorization \
  -H 'Content-Type: application/json' \
  -d '{
    "user": "analyst",
    "roles": ["ROLE_USER"],
    "workspace": "citydata",
    "layer": "buildings",
    "service": "WMS",
    "request": "GetMap"
  }'
```

## Rule Auditing and Maintenance

### Auditing Rules

Regularly review your rule set to ensure it remains effective:

1. Check for redundant rules
2. Look for conflicts or unexpected interactions
3. Verify that default rules are appropriate

### Exporting and Importing Rules

For backup or migration:

1. In the Admin UI, use "Export Rules" to download a JSON file
2. Use "Import Rules" to restore or migrate rules

Via REST API:

```bash
# Export all rules
curl -X GET http://localhost:8080/geoserver-acl/api/rules > rules_backup.json

# Import rules
curl -X POST \
  http://localhost:8080/geoserver-acl/api/rules/bulk \
  -H 'Content-Type: application/json' \
  -d @rules_backup.json
```

## Troubleshooting Common Issues

### Rule Not Applied

If a rule isn't being applied as expected:

1. Check rule priority - a higher priority rule might be overriding it
2. Verify the rule matches the request parameters exactly
3. Test the rule in isolation

### Conflicting Rules

If rules seem to conflict:

1. Review the priorities to ensure the correct precedence
2. Consider consolidating rules with similar effects
3. Use the "Test Rules" feature to see which rule is being applied

### Performance Issues

If rule evaluation is slow:

1. Reduce the number of rules
2. Optimize rule specificity - avoid too many wildcard matches
3. Review rule priorities for logical organization