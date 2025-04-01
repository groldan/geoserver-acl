#!/bin/bash

# Script to run MkDocs and generate Structurizr diagrams

# Check if Docker is available
if ! command -v docker &> /dev/null
then
    echo "Warning: Docker is not installed or not in PATH. Diagram generation will be skipped."
    SKIP_DIAGRAMS=true
else
    SKIP_DIAGRAMS=false
fi

# Get the script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Activate the virtual environment
if [ -d "${SCRIPT_DIR}/.venv" ]; then
    source "${SCRIPT_DIR}/.venv/bin/activate"
    echo "Virtual environment activated"
else
    echo "Error: Virtual environment not found. Please run ./setup_mkdocs.sh first."
    exit 1
fi

# Generate diagrams if Docker is available
if [ "$SKIP_DIAGRAMS" = false ]; then
    # Check if --skip-diagrams flag is provided
    if [[ ! "$*" == *"--skip-diagrams"* ]]; then
        echo "Generating diagrams with Structurizr..."
        cd "${SCRIPT_DIR}/docs/structurizr"
        ./generate-diagrams.sh
        cd "${SCRIPT_DIR}"
    else
        echo "Diagram generation skipped (--skip-diagrams flag provided)"
        # Remove the --skip-diagrams flag from the arguments
        args=()
        for arg in "$@"; do
            if [ "$arg" != "--skip-diagrams" ]; then
                args+=("$arg")
            fi
        done
        set -- "${args[@]}"
    fi
else
    echo "Diagram generation skipped (Docker not available)"
fi

# Make sure the config file exists
if [ ! -f "${SCRIPT_DIR}/mkdocs.yml" ]; then
    # Create the mkdocs.yml file in the root directory
    cp "${SCRIPT_DIR}/docs/mkdocs.yml" "${SCRIPT_DIR}/"
    
    # Update the docs_dir path if needed
    if ! grep -q "docs_dir:" "${SCRIPT_DIR}/mkdocs.yml"; then
        echo "docs_dir: docs" >> "${SCRIPT_DIR}/mkdocs.yml"
    fi
fi

# Run MkDocs with provided arguments (default to serve)
if [ "$#" -eq 0 ]; then
    echo "Running 'mkdocs serve'..."
    mkdocs serve
else
    echo "Running 'mkdocs $@'..."
    mkdocs "$@"
fi