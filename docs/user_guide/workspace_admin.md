# Workspace Administration

This page explains the special role of workspace administrators in GeoServer ACL and how to manage resources within your assigned workspaces.

## Understanding Workspace Administration

GeoServer ACL allows for delegated administration through workspace administrators. As a workspace administrator, you have limited administrative privileges that apply only to specific workspaces.

### What is a Workspace Administrator?

A workspace administrator is a user who has been granted administrative permissions for one or more specific workspaces in GeoServer, but not for the entire GeoServer instance. This enables a hierarchical administration model where:

- **GeoServer Administrators** have full control over the entire GeoServer instance
- **Workspace Administrators** can manage resources only within their assigned workspaces
- **Regular Users** have no administrative capabilities

### How Workspace Administration is Determined

Your status as a workspace administrator is determined by admin rules in GeoServer ACL. These rules specify:

1. Which users or roles have administrative access
2. Which workspaces they can administer
3. What level of administration they can perform

## Workspace Administrator Capabilities

As a workspace administrator, you typically can:

### 1. Manage Data Sources

- Add new stores (WMS, WFS, database connections, etc.) to your workspace
- Edit existing store configurations
- Remove stores from your workspace

### 2. Manage Layers

- Publish new layers from your workspace's data stores
- Edit layer properties, including:
  - Basic information (title, abstract)
  - Data settings
  - Publishing settings
  - Dimensions
  - Tile caching settings
- Remove layers from your workspace

### 3. Manage Layer Groups

- Create layer groups within your workspace
- Edit layer group configurations
- Remove layer groups

### 4. Manage Styles

- Create new styles for your workspace
- Edit existing styles within your workspace
- Remove styles from your workspace

### 5. View Workspace Information

- Access workspace settings
- View all resources within your workspace

## Limitations of Workspace Administration

As a workspace administrator, you typically cannot:

- Create, modify, or delete other workspaces
- Access administrative functions for other workspaces
- Modify global GeoServer settings
- Manage security settings (users, roles, services)
- Access server-level functions (logging, status, etc.)
- Create, modify, or delete global resources

## Accessing Workspace Administration

### Through the GeoServer Web Interface

1. Log in to the GeoServer Web interface
2. If you have workspace administration privileges, you'll see a limited set of options in the navigation panel
3. You'll only see workspaces you have permission to administer

![Workspace Admin UI](../assets/images/workspace_admin_ui.png)

### Through the REST API

As a workspace administrator, you can use the GeoServer REST API to manage your workspace resources. Your access will be limited to endpoints related to your authorized workspaces:

```
GET https://example.com/geoserver/rest/workspaces/your_workspace
```

## Common Administration Tasks

### Publishing a New Layer

1. Navigate to your workspace
2. Select or create a data store
3. Click "Publish" next to the resource you want to publish
4. Configure the layer properties
5. Click "Save"

### Creating a Layer Group

1. Navigate to "Layer Groups" in your workspace
2. Click "Add new layer group"
3. Set a name and add layers from your workspace
4. Arrange the layers in the desired order
5. Configure additional settings as needed
6. Click "Save"

### Creating a New Style

1. Navigate to "Styles" in your workspace
2. Click "Add a new style"
3. Choose a format (SLD, CSS, etc.)
4. Enter your style definition
5. Validate the style
6. Click "Submit"

## Best Practices for Workspace Administrators

1. **Organize your workspace logically**:
   - Group related layers together
   - Use consistent naming conventions
   - Create layer groups for commonly used combinations

2. **Document your resources**:
   - Add clear titles and abstracts to all resources
   - Include attribution information where appropriate
   - Add keywords to make resources discoverable

3. **Optimize performance**:
   - Configure appropriate scale denominators
   - Set up tile caching for frequently accessed layers
   - Use simplified geometries for high zoom levels

4. **Consider security implications**:
   - Remember that your published layers are subject to ACL rules
   - Coordinate with GeoServer administrators on security requirements
   - Be aware that sensitive attributes might be filtered by ACL rules

## Troubleshooting Access Issues

If you're a workspace administrator but can't perform certain functions:

1. **Verify your administrative rights**:
   - Confirm which workspaces you have permission to administer
   - Check if you have full or partial administrative rights

2. **Check for conflicts with ACL rules**:
   - Some operations might be blocked by additional ACL rules
   - Data access rules can still limit what you can do as an administrator

3. **Contact the GeoServer administrator**:
   - If you need additional permissions
   - If you're not seeing resources you expect to see
   - If you're encountering unexpected errors

## Related Information

- See [Rule Management](../admin_guide/rule_management.md) for understanding how rules work
- See [Admin Rule Management](../admin_guide/admin_rule_management.md) for how administrators set up workspace administration