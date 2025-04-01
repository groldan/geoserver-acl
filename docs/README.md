# GeoServer ACL Documentation

This directory contains the documentation for GeoServer ACL. The documentation is built using [MkDocs](https://www.mkdocs.org/) with the [Material](https://squidfunk.github.io/mkdocs-material/) theme.

## Building the Documentation

### Prerequisites

- Python 3.7 or later
- pip (Python package manager)

### Installation

1. Install MkDocs and the Material theme:

```bash
pip install mkdocs mkdocs-material
```

2. Install Structurizr CLI for generating architecture diagrams (optional):

```bash
# Download latest release from https://github.com/structurizr/cli/releases
# Extract and make executable
# Add to your PATH
```

### Building the Documentation

To build the documentation:

```bash
# From the docs directory
mkdocs build
```

This will create a `site` directory with the built documentation.

### Serving the Documentation Locally

To serve the documentation locally:

```bash
# From the docs directory
mkdocs serve
```

This will start a local development server at http://127.0.0.1:8000/.

## Documentation Structure

- `mkdocs.yml`: MkDocs configuration file
- `docs/`: Documentation source files
  - `index.md`: Home page
  - `user_guide/`: End user documentation
  - `admin_guide/`: Administrator documentation
  - `developer_guide/`: Developer documentation
  - `technical/`: Technical architecture documentation
  - `api/`: API reference documentation
  - `case_studies/`: Case studies and examples
  - `assets/`: Images, stylesheets, and other assets
  - `structurizr/`: Architecture diagrams in Structurizr DSL

## Generating Architecture Diagrams

If you have Structurizr CLI installed, you can generate the architecture diagrams:

```bash
# From the docs directory
cd structurizr
./generate-diagrams.sh
```

This will generate SVG diagrams in the `assets/images/structurizr` directory.

## Contributing

Contributions to the documentation are welcome! Please follow these guidelines:

1. Use Markdown for documentation files
2. Follow the existing structure
3. Use relative links for internal references
4. Place images in the appropriate subdirectory under `assets/images/`
5. Update the navigation in `mkdocs.yml` if adding new pages