# Contributing to GeoServer ACL Documentation

Thank you for your interest in contributing to the GeoServer ACL documentation! This guide will help you get started with the documentation process.

## Getting Started

1. Fork the repository on GitHub
2. Clone your fork locally
3. Set up the documentation environment following the instructions in the [README.md](README.md)

## Documentation Organization

The documentation is organized into several sections:

- **User's Guide**: Documentation aimed at end-users and administrators
- **Developer's Guide**: Documentation for developers who want to extend or modify GeoServer ACL
- **Technical Documentation**: Architecture documents following the arc42 template
- **API Documentation**: OpenAPI-based documentation for the REST APIs

## File Formats

- Main pages are written in **Markdown** (`.md`) files
- Comprehensive guides are written in **AsciiDoc** (`.adoc`) files
- API documentation is derived from the OpenAPI specification

## Making Changes

### For Small Changes

1. Navigate to the file you want to edit in your fork
2. Make your changes directly in the GitHub web editor
3. Commit your changes with a descriptive message
4. Create a pull request

### For Larger Changes

1. Create a topic branch in your local repository
2. Make your changes
3. Test by building the documentation locally using the build script
4. Commit your changes with descriptive messages
5. Push to your fork
6. Create a pull request

## Style Guidelines

### General Guidelines

- Use clear, concise language
- Explain technical terms when first used
- Use consistent terminology throughout
- Follow existing formatting patterns

### Markdown

- Use ATX-style headers (`#` for main heading, `##` for subheadings)
- Use backticks for inline code
- Use fenced code blocks with language specification
- Use relative links for cross-references

### AsciiDoc

- Follow the existing AsciiDoc structure in similar files
- Use the appropriate document attributes
- Use the appropriate heading levels
- Use AsciiDoc's table format for tabular data
- Use source blocks with language identifiers

## Working with Images

- Place images in the appropriate `images` directory for the section
- Use descriptive filenames
- Optimize images for web before adding
- Include alt text for images
- Use relative paths to reference images

## Testing Your Changes

Before submitting a pull request, test your documentation changes:

1. Build the documentation locally using the build script
2. Check for any build errors
3. Verify that your changes appear as expected in the generated HTML
4. Ensure navigation between pages works correctly

## Pull Request Process

1. Ensure your changes follow the style guidelines
2. Update any related documentation as needed
3. Create a pull request with a clear title and description
4. Respond to any feedback or suggestions from reviewers

## Getting Help

If you need help with the documentation or have questions, you can:

- Open an issue on GitHub
- Ask on the GeoServer mailing list
- Join the GeoServer community chat

Thank you for your contributions to improving GeoServer ACL documentation!