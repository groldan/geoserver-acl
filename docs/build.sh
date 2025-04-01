#!/bin/bash

# Get the script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"

# Use the main run_mkdocs.sh script to build the documentation
cd "${ROOT_DIR}"
./run_mkdocs.sh build