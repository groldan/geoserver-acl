#!/bin/bash

# This script sets up MkDocs for the GeoServer ACL documentation

# Exit on error
set -e

# Get the script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Create a virtual environment
echo "Creating a virtual environment for MkDocs..."
python3 -m venv .venv
source .venv/bin/activate

# Install MkDocs and required plugins
echo "Installing MkDocs and plugins..."
pip install -r "${SCRIPT_DIR}/docs/requirements.txt"

# Check if MkDocs was installed successfully
if ! .venv/bin/mkdocs --version &> /dev/null; then
    echo "Error: MkDocs installation failed"
    exit 1
fi

echo "MkDocs installed successfully"

# Check if docs directory structure exists
if [ ! -d "${SCRIPT_DIR}/docs/user_guide" ] || [ ! -d "${SCRIPT_DIR}/docs/technical" ] || [ ! -d "${SCRIPT_DIR}/docs/developer_guide" ]; then
    echo "Creating docs directory structure..."
    mkdir -p "${SCRIPT_DIR}/docs/user_guide" "${SCRIPT_DIR}/docs/technical" "${SCRIPT_DIR}/docs/developer_guide" 
    mkdir -p "${SCRIPT_DIR}/docs/admin_guide" "${SCRIPT_DIR}/docs/api" "${SCRIPT_DIR}/docs/case_studies"
fi

# Create missing directories for assets
mkdir -p "${SCRIPT_DIR}/docs/assets/images/structurizr"
mkdir -p "${SCRIPT_DIR}/docs/assets/stylesheets"
mkdir -p "${SCRIPT_DIR}/docs/structurizr/exports"

# Create directory for each section if it doesn't exist
touch_if_not_exists() {
    if [ ! -f "${SCRIPT_DIR}/docs/$1" ]; then
        echo "Creating placeholder: docs/$1"
        mkdir -p "$(dirname "${SCRIPT_DIR}/docs/$1")"
        echo "# $2" > "${SCRIPT_DIR}/docs/$1"
        echo "" >> "${SCRIPT_DIR}/docs/$1"
        echo "This page is under construction." >> "${SCRIPT_DIR}/docs/$1"
    fi
}

# User Guide
touch_if_not_exists "user_guide/index.md" "User Guide"
touch_if_not_exists "user_guide/access_control.md" "Understanding Access Control"
touch_if_not_exists "user_guide/ows_services.md" "Using OWS Services"
touch_if_not_exists "user_guide/workspace_admin.md" "Workspace Administration"
touch_if_not_exists "user_guide/troubleshooting.md" "Troubleshooting"

# Admin Guide
touch_if_not_exists "admin_guide/index.md" "Administrator Guide"
touch_if_not_exists "admin_guide/installation.md" "Installation"
touch_if_not_exists "admin_guide/configuration.md" "Configuration"
touch_if_not_exists "admin_guide/rule_management.md" "Rule Management"
touch_if_not_exists "admin_guide/admin_rule_management.md" "Admin Rule Management"
touch_if_not_exists "admin_guide/filtering.md" "Spatial & Attribute Filtering"
touch_if_not_exists "admin_guide/rest_api.md" "REST API Usage"
touch_if_not_exists "admin_guide/monitoring.md" "Monitoring"
touch_if_not_exists "admin_guide/geofence_migration.md" "Migration from GeoFence"

# Technical Documentation (arc42)
touch_if_not_exists "technical/index.md" "Technical Documentation"
touch_if_not_exists "technical/01-introduction-and-goals.md" "Introduction and Goals"
touch_if_not_exists "technical/02-architecture-constraints.md" "Architecture Constraints"
touch_if_not_exists "technical/03-system-scope-and-context.md" "System Scope and Context"
touch_if_not_exists "technical/04-solution-strategy.md" "Solution Strategy"
touch_if_not_exists "technical/05-building-block-view.md" "Building Block View"
touch_if_not_exists "technical/06-runtime-view.md" "Runtime View"
touch_if_not_exists "technical/07-deployment-view.md" "Deployment View"
touch_if_not_exists "technical/08-cross-cutting-concepts.md" "Cross-cutting Concepts"
touch_if_not_exists "technical/09-architecture-decisions.md" "Architecture Decisions"
touch_if_not_exists "technical/10-quality-requirements.md" "Quality Requirements"
touch_if_not_exists "technical/11-risks.md" "Risks and Technical Debt"
touch_if_not_exists "technical/12-glossary.md" "Glossary"

# Developer Guide
touch_if_not_exists "developer_guide/index.md" "Developer Guide"
touch_if_not_exists "developer_guide/architecture.md" "Architecture"
touch_if_not_exists "developer_guide/building.md" "Building from Source"
touch_if_not_exists "developer_guide/api_integration.md" "API Integration"
touch_if_not_exists "developer_guide/plugin_development.md" "Plugin Development"
touch_if_not_exists "developer_guide/release_guide.md" "Release Guide"
touch_if_not_exists "developer_guide/contributing.md" "Contributing"

# API Documentation
touch_if_not_exists "api/index.md" "API Reference"
touch_if_not_exists "api/rules.md" "Data Rules API"
touch_if_not_exists "api/admin_rules.md" "Admin Rules API"
touch_if_not_exists "api/authorization.md" "Authorization API"
touch_if_not_exists "api/client_examples.md" "Client Examples"

# Case Studies
touch_if_not_exists "case_studies/index.md" "Case Studies"
touch_if_not_exists "case_studies/telecom.md" "Telecommunications Field Data"
touch_if_not_exists "case_studies/environmental_agency.md" "Environmental Agency Data"
touch_if_not_exists "case_studies/utility.md" "Utility Company Asset Management"
touch_if_not_exists "case_studies/geoserver_cloud.md" "GeoServer Cloud Integration"

# Verification Checklist
touch_if_not_exists "VERIFICATION_CHECKLIST.md" "Verification Checklist for Documentation"

echo "MkDocs setup complete. To serve the documentation, run:"
echo "./run_mkdocs.sh"