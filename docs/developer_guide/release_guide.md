# Release Guide

This guide documents the process for creating and publishing releases of GeoServer ACL. It is intended for project maintainers who have the necessary permissions to perform releases.

GeoServer ACL offers both a manual release process and an automated release process using GitHub Actions.

## Version Numbering Strategy

GeoServer ACL follows [Semantic Versioning](https://semver.org/) (SemVer):

- **MAJOR version** (x.0.0): Incompatible API changes
- **MINOR version** (0.x.0): Backwards-compatible functionality
- **PATCH version** (0.0.x): Backwards-compatible bug fixes

Release candidates are suffixed with `-rc1`, `-rc2`, etc.

## Pre-Release Checklist

Before initiating a release, ensure:

- [ ] All critical issues for the target version are resolved
- [ ] All tests pass successfully (`./mvnw verify`)
- [ ] Code quality checks pass (`make lint`)
- [ ] Documentation is up-to-date with new features/changes
- [ ] Compatibility with the target GeoServer version(s) is verified
- [ ] Dependencies are up-to-date and secure
- [ ] Release notes draft is prepared
- [ ] Performance tests show no regressions

## Branch Management

The GeoServer ACL project maintains the following branches:

- `main`: Active development branch for the next minor/major release
- `x.y.z`: Maintenance branches for patch releases (e.g., `2.3.x`)

### Creating Release Branches

For a new minor/major release:

```bash
# Start from main
git checkout main
git pull origin main

# Create a new branch for the release line
git checkout -b X.Y.x
git push origin X.Y.x
```

For a patch release, use the existing maintenance branch:

```bash
git checkout X.Y.x
git pull origin X.Y.x
```

## Release Process

### 1. Prepare for the Release

1. Update the release version in all relevant files:

```bash
# Checkout the appropriate branch for the release
git checkout X.Y.x

# Ensure you have the latest changes
git pull origin X.Y.x
```

2. Create a release branch:

```bash
git checkout -b release/X.Y.Z
```

3. Update version numbers and documentation where applicable:

```bash
# Update version in README or other documentation if needed
# Ensure examples in documentation reflect the new version
```

4. Commit your changes:

```bash
git add -A
git commit -m "Prepare for release X.Y.Z"
```

### 2. Perform the Maven Release

GeoServer ACL uses Maven for dependency management and builds. The Maven Release Plugin helps automate the release process:

```bash
# Dry run to verify everything is correct
./mvnw release:prepare -DdryRun=true

# If the dry run is successful, clean up and perform the actual release
./mvnw release:clean
./mvnw release:prepare

# Perform the release (deploy artifacts to Maven repositories)
./mvnw release:perform
```

Parameters for the release:

- Release version: `X.Y.Z` (remove SNAPSHOT)
- SCM tag: `vX.Y.Z`
- New development version: `X.Y.(Z+1)-SNAPSHOT` for patch releases, or `X.(Y+1).0-SNAPSHOT` for minor releases

### 3. Build Docker Images

After the Maven release, build and push Docker images:

```bash
# Navigate to the Docker build directory
cd artifacts/api

# Build the Docker image with the release version
docker build -t geoserver/geoserver-acl:X.Y.Z -t geoserver/geoserver-acl:latest .

# Push the images to Docker Hub
docker push geoserver/geoserver-acl:X.Y.Z
docker push geoserver/geoserver-acl:latest
```

### 4. Create GitHub Release

1. Navigate to the [Releases page](https://github.com/geoserver/geoserver-acl/releases) on GitHub
2. Click "Draft a new release"
3. Select the tag version: `vX.Y.Z`
4. Title: "GeoServer ACL X.Y.Z"
5. Description: Paste the release notes
6. Attach artifacts (Optional):
   - Standalone jar file
   - GeoServer plugin ZIP file
7. If this is a pre-release, mark the "This is a pre-release" checkbox
8. Click "Publish release"

### 5. Update Documentation

1. Update the version references in the documentation:

```bash
# Switch to the docs branch
git checkout docs

# Update relevant files with the new version
# Look for version references in:
# - Installation guide
# - Building guide
# - API documentation
# - Docker instructions
```

2. Commit and push documentation changes:

```bash
git add -A
git commit -m "Update documentation for X.Y.Z release"
git push origin docs
```

### 6. Announce the Release

Once the release is complete:

1. Announce on the [GeoServer mailing list](https://lists.sourceforge.net/lists/listinfo/geoserver-users)
2. Post an announcement on the [GeoServer blog](https://geoserver.org/blog/)
3. Announce on social media channels

## Post-Release Tasks

After completing the release:

1. Merge the release branch back to the maintenance branch:

```bash
git checkout X.Y.x
git merge --no-ff release/X.Y.Z
git push origin X.Y.x
```

2. If relevant, backport changes to the main branch:

```bash
git checkout main
git cherry-pick <commit-hash> # Or use merge if appropriate
git push origin main
```

3. Delete the release branch:

```bash
git branch -d release/X.Y.Z
```

4. Create issues for the next release cycle

## Automated Release Process

GeoServer ACL provides an automated release process using GitHub Actions that handles the entire release workflow, including:

1. Version updates
2. Testing and validation
3. Artifact building and deployment
4. Docker image creation and publishing
5. Documentation updates
6. GitHub release creation

## Prerequisites for Automated Release

Before using the automated release process, ensure:

1. You have proper access to the GitHub repository
2. Required secrets are configured in the repository:
   
   - `OSSRH_USERNAME` and `OSSRH_TOKEN` for Maven Central deployment
   - `GPG_PRIVATE_KEY` and `GPG_PASSPHRASE` for signing artifacts
   - `DOCKERHUB_USERNAME` and `DOCKERHUB_TOKEN` for Docker Hub publishing

## Using the Automated Release Process

### Option 1: Using the Helper Script

The project includes a helper script (`prepare-release.sh`) that guides you through the release preparation and triggers the GitHub Actions workflow:

```bash
./prepare-release.sh
```

This script:

- Validates your local environment
- Prompts for release and next development versions
- Runs basic validation tests
- Triggers the GitHub Actions workflow with the provided parameters

### Option 2: Triggering the Workflow Manually

1. Navigate to the "Actions" tab in the GitHub repository
2. Select the "Release Process" workflow
3. Click "Run workflow"
4. Fill in the required parameters:
   
   - Release version (e.g., 1.2.3)
   - Next development version (e.g., 1.2.4-SNAPSHOT)
   - Whether this is a dry run
   - Whether to skip tests
5. Click "Run workflow"

## Monitoring the Automated Release

1. Monitor the GitHub Actions workflow execution in the "Actions" tab
2. The workflow provides detailed logs for each step of the release process
3. For dry runs, verify everything works as expected before running with `dryRun = false`

# Troubleshooting Common Issues

### Automated Release Workflow Problems

If the automated release workflow fails:

1. Check the GitHub Actions logs for detailed error information
2. If it's a dry run, fix the issues and try again
3. If it's a real release that failed, you may need to:
   
   - Delete the created tag: `git push --delete origin vX.Y.Z`
   - Reset your local branch: `git reset --hard origin/main`
   - Try again with the fixed configuration

### Maven Release Problems

If the Maven release fails:

1. Check the error message carefully
2. Clean up the failed release:

```bash
./mvnw release:rollback
./mvnw release:clean
```

3. Fix any issues
4. Try again from the "Perform the Maven Release" step

### Docker Image Issues

If Docker image building fails:

1. Verify Docker is running and you have sufficient permissions
2. Check that all dependencies are correctly configured in the Dockerfile
3. Try building without cache: `docker build --no-cache -t ...`

### GitHub Release Issues

If GitHub release creation fails:

1. Verify you have the correct permissions
2. Check that the tag exists and matches exactly
3. Try creating the release from the command line using the GitHub CLI: 
   ```
   gh release create vX.Y.Z --title "GeoServer ACL X.Y.Z" --notes "..."
   ```

## Special Release Types

### Release Candidates

For a release candidate:

1. Follow the same process but use `-rc1` suffix in version numbers
2. Clearly mark as pre-release on GitHub
3. Be explicit in announcements that this is a testing release

### Patch Releases

For patch releases (bug fixes):

1. Cherry-pick fixes from main into the maintenance branch
2. Follow the standard release process on the maintenance branch
3. Focus release notes on bug fixes and security patches

### Major Releases

Major releases with breaking changes require additional steps:

1. Update migration guides in documentation
2. Provide code examples for any API changes
3. Consider a longer RC phase for testing
4. Prepare detailed upgrade instructions

## Appendix: Release Notes Template

```markdown
# GeoServer ACL X.Y.Z

Release date: YYYY-MM-DD

## Overview

Brief description of the release focus (new features, bug fixes, etc.)

## New Features

- Feature 1: Description
- Feature 2: Description

## Improvements

- Improvement 1: Description
- Improvement 2: Description

## Bug Fixes

- Issue #XXX: Description
- Issue #YYY: Description

## Breaking Changes (if applicable)

- Change 1: Description and migration path
- Change 2: Description and migration path

## Security Updates

- CVE-XXXX-XXXX: Description

## Upgrade Instructions

Special instructions for upgrading from previous versions.

## Contributors

Thanks to the following contributors for this release:
- Contributor 1
- Contributor 2
```