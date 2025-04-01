# Troubleshooting

This page provides guidance on common issues you might encounter when accessing resources protected by GeoServer ACL, and how to resolve them.

## Common Access Issues

### "Access Denied" Errors

If you receive a "Access Denied" error when trying to access a layer or service:

#### Possible Causes:
- You don't have permission to access the requested resource
- You're trying to use a service (e.g., WFS) that you don't have permission for
- Your spatial query is completely outside your allowed area
- You haven't authenticated properly

#### Solutions:

1. **Check your authentication**:
   - Ensure you're properly logged in
   - Verify your credentials are correct
   - Check that your authentication method is working

2. **Verify your permissions**:
   - Ask your administrator which resources you have access to
   - Confirm which services you're allowed to use

3. **Check spatial limitations**:
   - If you have spatially limited access, ensure your query includes areas you can access
   - Try a broader spatial query to see if any portion is accessible

### Missing Layers

If you don't see layers you expect in the capabilities document:

#### Possible Causes:

- You don't have permission to access those layers
- The layers are in a workspace you don't have access to
- The layers exist but are restricted by ACL rules

#### Solutions:

1. **Check with your administrator**:
   - Ask which layers you should have access to
   - Verify your role assignments

2. **Check for workspace restrictions**:
   - You might only have access to specific workspaces

3. **Look for misspellings**:
   - Make sure you're using the correct layer names

### Incomplete or Filtered Data

If you can access a layer but the data seems incomplete:

#### Possible Causes:

- Your view is being spatially filtered to a specific area
- Certain attributes are being filtered out
- You're hitting result limits enforced by ACL

#### Solutions:

1. **Check for spatial limitations**:
   - Your view might be limited to a specific geographic area
   - Try visualizing the full extent to see where your data is cut off

2. **Review attribute access**:
   - Some attributes might be hidden from your view
   - Check with your administrator about attribute-level permissions

3. **Adjust your queries**:
   - Make smaller, more focused queries
   - Add spatial filters to match your allowed area

### Service-Specific Issues

#### WMS Issues:
- **Blank or Partial Maps**: You may have spatial restrictions
- **Missing Layers in Legend**: You don't have permission for those layers
- **Style Limitations**: Some styles might be restricted

#### WFS Issues:
- **Empty Results**: Your query area might be outside your allowed region
- **Missing Attributes**: Some attributes might be filtered out
- **Transaction Failures**: You might not have write permission

#### WCS Issues:
- **Resolution Limitations**: Access to high-resolution data might be restricted
- **Format Restrictions**: Some output formats might be unavailable

## Authentication Issues

### Basic Authentication Problems

If you're using Basic Authentication and experiencing issues:

1. **Ensure credentials are correct**:
   - Username/password are case-sensitive
   - Check for typos or copy-paste errors

2. **Check authentication headers**:
   - Your client should be sending proper `Authorization: Basic ...` headers
   - Ensure headers aren't being stripped by proxies

### OAuth/OpenID Connect Issues

If you're using OAuth2 or OpenID Connect:

1. **Check token validity**:
   - Tokens expire - ensure your client refreshes them
   - Verify the token scope includes necessary permissions

2. **Review client configuration**:
   - Ensure redirect URIs are correctly set
   - Check that client ID and secret are valid

## Client-Specific Troubleshooting

### QGIS

If you're having issues accessing GeoServer resources from QGIS:

1. **Authentication settings**:
   - In the connection settings, ensure authentication is properly configured
   - Try saving authentication credentials with your connection

2. **Layer properties**:
   - Check the layer properties for any request errors
   - Look for WFS/WMS error messages in the QGIS log

3. **QGIS-specific solutions**:
   - Try creating a new connection from scratch
   - Use the "Test Connection" button to diagnose issues
   - Update to the latest QGIS version

### Web Clients (OpenLayers, Leaflet, etc.)

For web mapping libraries:

1. **CORS issues**:
   - Ensure GeoServer is configured to allow CORS if needed
   - Check browser console for CORS errors

2. **Authentication handling**:
   - Implement proper auth token management
   - Use appropriate auth headers in all requests

3. **Error handling**:
   - Implement graceful error handling for 403 errors
   - Add user-friendly messages when access is denied

## Getting Help from Administrators

If you continue to experience access issues:

1. **Gather diagnostic information**:
   - Record the exact URL you're trying to access
   - Note any error messages you receive
   - Document which resources you can and cannot access
   - Capture screenshots of errors

2. **Contact your administrator with**:
   - Your username and roles
   - What you're trying to access
   - The diagnostic information you've gathered
   - Steps you've already tried

3. **Ask specific questions**:
   - "Which layers should I have access to?"
   - "Do I have spatial limitations on my view?"
   - "Which services am I allowed to use?"

## Advanced Troubleshooting

### Testing Access via cURL

You can test direct access to OWS services using cURL:

```bash
# Test WMS access
curl -u username:password "https://example.com/geoserver/wms?service=WMS&version=1.3.0&request=GetCapabilities"

# Test WFS access
curl -u username:password "https://example.com/geoserver/wfs?service=WFS&version=2.0.0&request=GetCapabilities"
```

### Checking GeoServer Logs

If you have access to GeoServer logs, look for:

- Lines containing "GeoServer ACL" or "Access Control"
- Error messages related to authorization
- Entries showing rule evaluation

Logs can help identify:

- Which rules are being applied
- Why access is being denied
- Authentication problems