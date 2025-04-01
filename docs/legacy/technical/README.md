# GeoServer ACL Technical Documentation

This directory contains technical documentation for the GeoServer ACL project, following the [arc42](https://arc42.org/) template and using [C4 model](https://c4model.com/) diagrams created with [Structurizr](https://structurizr.com/).

## Documentation Structure

The documentation follows the arc42 template structure:

1. [Introduction and Goals](01-introduction-and-goals.adoc)
2. [Architecture Constraints](02-architecture-constraints.adoc)
3. [System Scope and Context](03-system-scope-and-context.adoc)
4. [Solution Strategy](04-solution-strategy.adoc)
5. [Building Block View](05-building-block-view.adoc)
6. [Runtime View](06-runtime-view.adoc)
7. [Deployment View](07-deployment-view.adoc)
8. [Cross-cutting Concepts](08-cross-cutting-concepts.adoc)
9. [Architecture Decisions](09-architecture-decisions.adoc)
10. [Quality Requirements](10-quality-requirements.adoc)
11. [Technical Risks](11-risks.adoc)
12. [Glossary](12-glossary.adoc)

## C4 Diagrams with Structurizr

The C4 model diagrams are defined using the Structurizr DSL in the [structurizr](./structurizr) directory.

To view or edit these diagrams:

1. Install Structurizr CLI: https://github.com/structurizr/cli
2. Run the local Structurizr Lite server:
   ```
   structurizr-cli lite --workspace ./structurizr/workspace.dsl
   ```
3. Open http://localhost:8080 in your browser

## Building the Documentation

To build the documentation as HTML:

1. Install Asciidoctor: `gem install asciidoctor asciidoctor-diagram`
2. Run the build script: `./build.sh`
3. HTML output will be generated in the `build` directory

## Contributing to the Documentation

When contributing to this documentation:

1. Follow the arc42 template structure
2. Update diagrams in the Structurizr DSL when making architecture changes
3. Ensure all documentation is consistent with the current implementation
4. Add references to source code where appropriate