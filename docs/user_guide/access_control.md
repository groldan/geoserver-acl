# Understanding Access Control

This page explains how GeoServer ACL's access control system works, helping you understand how your access to GeoServer resources is determined.

## Access Control Basics

GeoServer ACL uses a rule-based system to determine what resources you can access. Administrators define rules that specify:

1. **Who** can access resources (users and roles)
2. **What** resources they can access (workspaces, layers, etc.)
3. **How** they can access them (view, download, edit)
4. **Where** they can access data (geographic limitations)
5. **Which attributes** they can access

These rules are evaluated for each request you make to GeoServer.

## Types of Rules

### Data Access Rules

Data access rules control your ability to view and interact with GeoServer data. They can:

- **Grant** access: Allow you to access specific resources
- **Deny** access: Explicitly prevent access to resources
- **Limit** access: Allow access with specific restrictions (spatial, attribute, etc.)

### Admin Rules

Admin rules control your ability to administer GeoServer resources through the Admin UI or REST API. They determine:

- Whether you're considered a workspace administrator
- Which workspaces you can administer
- What administrative actions you can take

## How Rules Are Resolved

When you make a request to GeoServer, the ACL authorization process follows these steps:

1. Identifies all rules that match your request (based on user, role, service, layer, etc.)
2. Sorts matching rules by priority (administrators assign priorities to rules)
3. Applies the highest-priority matching rule
4. If no rules match, applies the default rule (typically DENY)

### Rule Specificity

More specific rules generally have higher priority than general rules. For example:

- A rule for a specific user overrides a rule for a role
- A rule for a specific layer overrides a rule for a workspace
- A rule for a specific service (e.g., WMS) overrides a general rule

## Types of Access Restrictions

### Layer Visibility

The most basic restriction is whether you can see a layer at all:

- If denied access, the layer won't appear in capabilities documents
- If granted access, the layer will be visible

### Spatial Restrictions

Your view of data may be limited to specific geographic areas:

- Data outside your allowed area will be clipped or filtered out
- Requests for data entirely outside your allowed area may be denied

![Spatial Restriction](../assets/images/spatial_restriction.png){: style="width:400px"}

### Attribute Filtering

Access to specific attributes (columns in a dataset) may be restricted:

- Hidden attributes won't appear in feature information or downloads
- Write access to attributes may be restricted separately from read access

### Service Restrictions

Different levels of access may be granted for different OGC services:

- You might be able to view maps (WMS) but not download data (WFS)
- Certain operations within a service may be restricted (e.g., WFS-T transactions)

## Access Control in Practice

### Example Scenario

Consider a municipal GIS where:

1. The **Public** can view basic map layers through WMS
2. **Department Staff** can download data for their department's area through WFS
3. **GIS Technicians** can edit data in specific areas
4. **Department Managers** can administer their department's workspace

Each user will have a different experience based on their assigned permissions.

### How It Affects Your Workflow

As a user, the main impacts on your workflow will be:

- You'll only see resources you have access to in capabilities documents
- Spatial queries will only return data from your allowed areas
- Feature information and attribute tables will only show authorized attributes
- Edit operations will be restricted to your allowed areas and attributes
- Administration UI will only show workspaces you can administer

## Understanding Access Errors

If you encounter access errors, they typically mean:

- You're trying to access a resource you don't have permission for
- You're trying to perform an action you don't have permission for
- Your spatial query is outside your allowed area
- You're trying to edit restricted attributes

See the [Troubleshooting](troubleshooting.md) section for more information on handling access errors.