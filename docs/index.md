# GeoServer ACL Documentation

![GeoServer ACL Logo](assets/images/logo.svg){: style="display: block; margin: 0 auto; max-width: 300px;"}

## What is GeoServer ACL?

GeoServer ACL is an advanced authorization system for [GeoServer](https://geoserver.org/), providing fine-grained access control to geospatial resources. It consists of:

1. An independent application service that manages access rules
2. A GeoServer plugin that applies these rules on a per-request basis

## Key Features

- **Fine-grained Authorization**: Control access at the workspace, layer, feature, and attribute levels
- **Spatial Filtering**: Limit users to specific geographic areas
- **Attribute Filtering**: Control which attributes users can access
- **Service-based Restrictions**: Different rules for different OWS services (WMS, WFS, WCS)
- **Administration Controls**: Granular workspace administration privileges
- **Flexible Rule System**: Prioritized rules with cascading effects
- **REST API**: Programmatic access for integration with other systems

## How it Works

As an administrator, you define rules that grant or deny access to GeoServer resources based on:

- User identity and roles
- Requested layers and workspaces
- Service types (WMS, WFS, WCS, etc.)
- Request operations (GetMap, GetFeature, etc.)
- Client IP address

When users access GeoServer, the ACL authorization engine evaluates these rules to determine:

- Which resources they can see
- What geographic areas they can access
- Which attributes are visible
- What administrative actions they can perform

![Authorization Flow](assets/images/acl.svg)

## Getting Started

Choose the guide that best fits your role:

- [**User Guide**](user_guide/index.md): For end users accessing GeoServer services
- [**Administrator Guide**](admin_guide/index.md): For setting up and managing GeoServer ACL
- [**Developer Guide**](developer_guide/index.md): For developers integrating with or extending GeoServer ACL
- [**Technical Documentation**](technical/index.md): For in-depth architectural information
- [**API Reference**](api/index.md): For programmatic integration

## About GeoServer ACL

GeoServer ACL is open source software, created as a fork of [GeoFence](https://github.com/geoserver/geofence). It follows the same logical approach to rule definition while providing additional features and improvements.

If you're familiar with GeoFence, transitioning to GeoServer ACL will be straightforward. For a comparison and migration guide, see [Migration from GeoFence](admin_guide/geofence_migration.md).

## Community and Support

GeoServer ACL is maintained by the GeoServer community as part of the broader GeoServer ecosystem. It is licensed under the GPL 2.0 license.

- [GitHub Repository](https://github.com/geoserver/geoserver-acl)
- [Issue Tracker](https://github.com/geoserver/geoserver-acl/issues)
- [GeoServer Community](https://geoserver.org/comm/)