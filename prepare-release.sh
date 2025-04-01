#!/bin/bash

# Script to prepare for a GeoServer ACL release
# This script runs pre-release checks and prepares the environment

set -e  # Exit on error

# Get the script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# Check if running in interactive mode
if [ -t 0 ]; then
  INTERACTIVE=true
else
  INTERACTIVE=false
fi

# Function to prompt for input with a default value
prompt() {
  local prompt_text="$1"
  local default_value="$2"
  local var_name="$3"
  local response
  
  if [ "$INTERACTIVE" = true ]; then
    read -p "$prompt_text [$default_value]: " response
    response="${response:-$default_value}"
  else
    echo "$prompt_text: using default [$default_value]"
    response="$default_value"
  fi
  
  eval "$var_name=\"$response\""
}

# Function to validate semantic version
validate_version() {
  local version="$1"
  local is_snapshot="$2"
  
  # Basic semver validation (X.Y.Z format)
  if ! [[ "$version" =~ ^[0-9]+\.[0-9]+\.[0-9]+(-SNAPSHOT)?$ ]]; then
    echo "Error: Version must follow semantic versioning (X.Y.Z)"
    return 1
  fi
  
  # Check if it should be a SNAPSHOT version
  if [ "$is_snapshot" = true ] && [[ "$version" != *-SNAPSHOT ]]; then
    echo "Error: Development version must end with -SNAPSHOT"
    return 1
  elif [ "$is_snapshot" = false ] && [[ "$version" == *-SNAPSHOT ]]; then
    echo "Error: Release version cannot end with -SNAPSHOT"
    return 1
  fi
  
  return 0
}

# Get current version from Maven
CURRENT_VERSION=$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout)
echo "Current version: $CURRENT_VERSION"

# Remove -SNAPSHOT from current version to suggest as release version
SUGGESTED_RELEASE_VERSION=${CURRENT_VERSION%-SNAPSHOT}

# Increment patch version for next development version suggestion
IFS='.' read -r major minor patch <<< "${SUGGESTED_RELEASE_VERSION}"
if [[ "$patch" == *-* ]]; then
  # Handle pre-release versions (e.g., 1.2.3-beta)
  pre_release="${patch#*-}"
  patch="${patch%-*}"
  NEXT_PATCH="$patch-$pre_release"
else
  NEXT_PATCH=$((patch + 1))
fi
SUGGESTED_NEXT_VERSION="$major.$minor.$NEXT_PATCH-SNAPSHOT"

# Get release version
prompt "Enter release version" "$SUGGESTED_RELEASE_VERSION" RELEASE_VERSION
if ! validate_version "$RELEASE_VERSION" false; then
  exit 1
fi

# Get next development version
prompt "Enter next development version" "$SUGGESTED_NEXT_VERSION" NEXT_DEV_VERSION
if ! validate_version "$NEXT_DEV_VERSION" true; then
  exit 1
fi

# Check if this is a dry run
if [ "$INTERACTIVE" = true ]; then
  read -p "Is this a dry run? (no changes will be pushed) [Y/n]: " IS_DRY_RUN
  IS_DRY_RUN=${IS_DRY_RUN:-Y}
  if [[ "$IS_DRY_RUN" =~ ^[Yy]$ ]]; then
    DRY_RUN=true
  else
    DRY_RUN=false
  fi
else
  DRY_RUN=true
  echo "Defaulting to dry run mode (no changes will be pushed)"
fi

# Check if tests should be skipped
if [ "$INTERACTIVE" = true ]; then
  read -p "Skip tests? [y/N]: " SKIP_TESTS_INPUT
  SKIP_TESTS_INPUT=${SKIP_TESTS_INPUT:-N}
  if [[ "$SKIP_TESTS_INPUT" =~ ^[Yy]$ ]]; then
    SKIP_TESTS=true
  else
    SKIP_TESTS=false
  fi
else
  SKIP_TESTS=false
  echo "Running tests by default"
fi

# Validate local environment
echo "Validating local environment..."

# Check if Maven is installed
if ! command -v ./mvnw &> /dev/null; then
  echo "Maven wrapper not found. Please run from the root of the GeoServer ACL repository."
  exit 1
fi

# Check if Git is installed
if ! command -v git &> /dev/null; then
  echo "Git is not installed or not in PATH."
  exit 1
fi

# Check if on correct branch
CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)
if [ "$CURRENT_BRANCH" != "main" ] && [ "$INTERACTIVE" = true ]; then
  read -p "Not on main branch. Current branch is $CURRENT_BRANCH. Continue? [y/N]: " CONTINUE
  CONTINUE=${CONTINUE:-N}
  if [[ ! "$CONTINUE" =~ ^[Yy]$ ]]; then
    exit 1
  fi
fi

# Check for uncommitted changes
if [ -n "$(git status --porcelain)" ]; then
  echo "Warning: There are uncommitted changes in the repository."
  if [ "$INTERACTIVE" = true ]; then
    read -p "Continue with uncommitted changes? [y/N]: " CONTINUE_UNCOMMITTED
    CONTINUE_UNCOMMITTED=${CONTINUE_UNCOMMITTED:-N}
    if [[ ! "$CONTINUE_UNCOMMITTED" =~ ^[Yy]$ ]]; then
      exit 1
    fi
  else
    echo "Aborting due to uncommitted changes."
    exit 1
  fi
fi

# Run basic validation
echo "Running basic validation..."

if [ "$SKIP_TESTS" = false ]; then
  echo "Running tests (this may take a while)..."
  ./mvnw clean verify -ntp -T4 || {
    echo "Tests failed. Please fix test failures before releasing."
    exit 1
  }
else
  echo "Skipping tests as requested."
fi

echo "Running style checks..."
./mvnw sortpom:verify fmt:check -ntp || {
  echo "Style check failed. Please run 'make format' to fix style issues."
  exit 1
}

# Launch GitHub Actions workflow
if [ "$INTERACTIVE" = true ]; then
  read -p "Launch GitHub Actions release workflow now? [Y/n]: " LAUNCH_WORKFLOW
  LAUNCH_WORKFLOW=${LAUNCH_WORKFLOW:-Y}
  if [[ "$LAUNCH_WORKFLOW" =~ ^[Yy]$ ]]; then
    if command -v gh &> /dev/null; then
      echo "Launching GitHub Actions workflow..."
      gh workflow run release.yml -f releaseVersion="$RELEASE_VERSION" -f nextDevelopmentVersion="$NEXT_DEV_VERSION" -f dryRun="$DRY_RUN" -f skipTests="$SKIP_TESTS"
      echo "Workflow started. Check GitHub Actions tab for progress."
    else
      echo "GitHub CLI (gh) not installed. Please install it or manually trigger the workflow."
      echo "Parameters to use:"
      echo "  releaseVersion: $RELEASE_VERSION"
      echo "  nextDevelopmentVersion: $NEXT_DEV_VERSION"
      echo "  dryRun: $DRY_RUN"
      echo "  skipTests: $SKIP_TESTS"
    fi
  fi
else
  echo "Skipping GitHub Actions workflow launch in non-interactive mode."
  echo "To launch the workflow, run:"
  echo "  gh workflow run release.yml -f releaseVersion=\"$RELEASE_VERSION\" -f nextDevelopmentVersion=\"$NEXT_DEV_VERSION\" -f dryRun=\"$DRY_RUN\" -f skipTests=\"$SKIP_TESTS\""
fi

echo "Release preparation complete."
echo "Release Version: $RELEASE_VERSION"
echo "Next Development Version: $NEXT_DEV_VERSION"
echo "Dry Run: $DRY_RUN"
echo "Skip Tests: $SKIP_TESTS"
echo ""
echo "Next steps:"
echo "1. Monitor the GitHub Actions workflow"
echo "2. Verify the release artifacts"
echo "3. If this was a dry run, trigger the actual release by running the workflow with dryRun=false"