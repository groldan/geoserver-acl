# Introduction and Goals

## Overview

GeoServer ACL (Access Control List) is an advanced authorization system for [GeoServer](https://geoserver.org/), providing fine-grained access control to geospatial resources. It consists of an independent application service that manages access rules and a GeoServer plugin that enforces these rules on a per-request basis.

GeoServer ACL was created as a fork of [GeoFence](https://github.com/geoserver/geofence), with improvements to architecture, performance, and integration capabilities. It maintains compatibility with GeoFence's core concepts while enhancing flexibility and adding support for cloud-native deployments.

## Requirements Overview

GeoServer ACL addresses the following key requirements:

### Functional Requirements

1. **Fine-grained Authorization**: Control access at the workspace, layer, feature, and attribute levels based on user identity and request properties.

2. **Spatial Filtering**: Limit users' access to specific geographic areas by applying spatial filters to data.

3. **Attribute Filtering**: Control which attributes (data fields) users can access within layers.

4. **Service-based Restrictions**: Apply different access control rules based on OGC service type (WMS, WFS, WCS, etc.) and operation.

5. **Workspace Administration**: Define administrative permissions for workspaces, allowing delegation of administration tasks.

6. **Programmatic Integration**: Provide APIs for integrating with external systems for rule management and authorization.

7. **Integration with Authentication Systems**: Work with any authentication system that GeoServer supports (Basic HTTP, OAuth2, OpenID Connect, etc.).

### Non-Functional Requirements

1. **Performance**: Minimize impact on GeoServer request processing time while applying complex access control rules.

2. **Scalability**: Support high-volume deployments with numerous users, layers, and rules.

3. **Cloud Compatibility**: Function well in cloud environments and containerized deployments.

4. **Maintainability**: Keep a clean, modular codebase that's easy to understand and extend.

5. **Backward Compatibility**: Maintain functional compatibility with GeoFence for migration purposes.

## Quality Goals

The following table outlines the top quality goals for GeoServer ACL:

| Priority | Quality Goal      | Motivation                                                                 |
|----------|-------------------|----------------------------------------------------------------------------|
| 1        | Performance       | Authorization decisions must be fast to avoid impact on GeoServer response time |
| 2        | Flexibility       | Support diverse deployment models from standalone to cloud-native          |
| 3        | Modularity        | Allow components to be used independently and support different integrations |
| 4        | Reliability       | Authorization system must work consistently and predictably                |
| 5        | Testability       | Easy to verify correct rule application and diagnose issues                |
| 6        | Evolvability      | Architecture should support future enhancements without major redesigns    |

## Stakeholders

The following table identifies the main stakeholders of GeoServer ACL and their concerns:

| Role                        | Description                                    | Expectations/Concerns                                    |
|-----------------------------|------------------------------------------------|----------------------------------------------------------|
| GeoServer Users             | End users accessing GeoServer resources        | Reliable access to authorized resources; clear feedback when access is denied |
| GeoServer Administrators    | Configure and manage GeoServer and ACL rules   | Easy rule management; predictable rule application; performance monitoring |
| System Integrators          | Integrate GeoServer ACL with other systems     | Clear, stable API; good documentation; extensibility     |
| GeoServer Developers        | Contribute to GeoServer and its plugins        | Clean code; compatibility with GeoServer architecture    |
| Organization Data Managers  | Manage spatial data access organization-wide   | Flexible rules to implement complex policies; audit capability |