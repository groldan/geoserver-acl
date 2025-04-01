# Glossary

This glossary defines the key terms used throughout the GeoServer ACL documentation and codebase. It provides a common language for developers, administrators, and users.

## Domain-Specific Terms

| Term | Definition |
|------|------------|
| **ACL** | Access Control List. In this context, it refers to the system that controls access to GeoServer resources. |
| **Rule** | A definition that specifies who can access what GeoServer resources under what conditions. Rules are evaluated in priority order. |
| **AdminRule** | A definition that specifies who can perform administrative operations on GeoServer. |
| **Priority** | A numeric value that determines the order in which rules are evaluated. Higher priority rules are evaluated first. |
| **LayerDetails** | Extended access control information associated with a rule, providing fine-grained control over layer access. |
| **LayerAttribute** | A specification of access restrictions for a specific attribute within a layer. |
| **RuleLimits** | Constraints applied to a rule, such as spatial limits or allowed CRS. |
| **GrantType** | The type of access granted by a rule: ALLOW, DENY, or LIMIT. |
| **AdminGrantType** | The type of administrative access granted: ADMIN, USER, or GROUP. |
| **CatalogMode** | How unauthorized access to catalog resources is handled: HIDE, MIXED, or CHALLENGE. |
| **SpatialFilterType** | The type of spatial filter to apply: INTERSECT or CLIP. |
| **AccessRequest** | A request for authorization to access a GeoServer resource. |
| **AccessInfo** | The result of an authorization decision, containing grants and limitations. |
| **AccessSummary** | A summary of access permissions across multiple workspaces or layers. |
| **AdminAccessRequest** | A request for authorization to perform administrative operations. |
| **AdminAccessInfo** | The result of an administrative authorization decision. |
| **WorkspaceAccessSummary** | A summary of access permissions for a specific workspace. |

## Technical Terms

| Term | Definition |
|------|------------|
| **API** | Application Programming Interface. A set of rules that allow programs to communicate with each other. |
| **REST** | Representational State Transfer. An architectural style for distributed systems, used for the ACL API. |
| **OpenAPI** | A specification for machine-readable interface files for describing, producing, consuming, and visualizing RESTful web services. |
| **JPA** | Java Persistence API. A Java specification for accessing, persisting, and managing data between Java objects and a relational database. |
| **ORM** | Object-Relational Mapping. A technique that converts data between incompatible type systems in object-oriented programming languages. |
| **DTO** | Data Transfer Object. An object that carries data between processes. |
| **Spring Boot** | An extension of the Spring framework that simplifies the development of Java applications. |
| **Spring Cloud** | A framework that provides tools for developers to quickly build common distributed system patterns. |
| **PostGIS** | A spatial database extender for PostgreSQL, adding support for geographic objects. |
| **Hibernate** | An object-relational mapping tool for Java. |
| **Docker** | A platform for developing, shipping, and running applications in containers. |
| **Microservice** | A software development approach where applications are built as a collection of small, independent services. |
| **JWT** | JSON Web Token. A compact, URL-safe means of representing claims to be transferred between two parties. |
| **JNDI** | Java Naming and Directory Interface. A Java API for a directory service that allows Java software clients to discover and look up data and resources. |
| **Maven** | A build automation tool used primarily for Java projects. |

## GeoServer Terms

| Term | Definition |
|------|------------|
| **GeoServer** | An open-source server for sharing geospatial data, implementing OGC standards. |
| **Workspace** | In GeoServer, a workspace is a container for grouping similar data. |
| **Layer** | A collection of geographic features or raster data with the same attributes and spatial reference. |
| **FeatureType** | A vector-based spatial resource or data set representing a specific type of feature. |
| **Coverage** | A raster-based spatial resource or data set. |
| **WMS** | Web Map Service. A standard protocol for serving georeferenced map images over the Internet. |
| **WFS** | Web Feature Service. A standard protocol for serving vector features over the Internet. |
| **WCS** | Web Coverage Service. A standard protocol for serving raster data over the Internet. |
| **OGC** | Open Geospatial Consortium. A standards organization that develops open standards for geospatial content. |
| **SLD** | Styled Layer Descriptor. An XML schema for describing the appearance of map layers. |
| **CRS** | Coordinate Reference System. A coordinate-based local, regional or global system used to locate geographical entities. |
| **EPSG** | European Petroleum Survey Group. A collection of CRS definitions. |
| **ResourceAccessManager** | A GeoServer interface that controls access to resources. ACL implements this interface. |

## Project-Specific Abbreviations

| Abbreviation | Definition |
|--------------|------------|
| **ACL** | Access Control List |
| **GS** | GeoServer |
| **JPA** | Java Persistence API |
| **API** | Application Programming Interface |
| **REST** | Representational State Transfer |
| **DTO** | Data Transfer Object |
| **ORM** | Object-Relational Mapping |
| **CRS** | Coordinate Reference System |
| **WMS** | Web Map Service |
| **WFS** | Web Feature Service |
| **WCS** | Web Coverage Service |
| **WPS** | Web Processing Service |
| **OGC** | Open Geospatial Consortium |

## Design Patterns and Concepts

| Term | Definition |
|------|------------|
| **DDD** | Domain-Driven Design. A software design approach focusing on the core domain and domain logic. |
| **Repository Pattern** | A design pattern that separates the logic that retrieves data from the underlying storage. |
| **Builder Pattern** | A design pattern that constructs complex objects step by step. |
| **Adapter Pattern** | A design pattern that allows classes with incompatible interfaces to work together. |
| **Factory Pattern** | A design pattern that creates objects without exposing the instantiation logic. |
| **Value Object** | An immutable object that contains attributes but has no conceptual identity. |
| **Entity** | An object fundamentally defined by its identity rather than its attributes. |
| **Aggregate** | A cluster of domain objects that can be treated as a single unit. |
| **Bounded Context** | A boundary within which a particular domain model applies. |
| **Hexagonal Architecture** | An architectural pattern that puts inputs and outputs at the edges of the design. Also known as Ports and Adapters. |
| **Microservice Architecture** | An architectural style that structures an application as a collection of loosely coupled services. |