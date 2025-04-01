# Using OWS Services

This page explains how GeoServer ACL affects your use of various OGC Web Services (OWS) provided by GeoServer.

## Overview of OGC Services

GeoServer supports multiple OGC (Open Geospatial Consortium) service standards, each with different purposes:

- **WMS** (Web Map Service): For viewing maps as images
- **WFS** (Web Feature Service): For accessing vector feature data
- **WCS** (Web Coverage Service): For accessing raster coverage data
- **WMTS** (Web Map Tile Service): For accessing pre-rendered map tiles
- **WPS** (Web Processing Service): For running geospatial processes
- **CSW** (Catalog Service for the Web): For discovering and accessing metadata

GeoServer ACL can control access to each of these services independently.

## WMS - Web Map Service

WMS is used for requesting and viewing maps.

### How ACL Affects WMS

1. **GetCapabilities**: You'll only see layers you have access to in the capabilities document
2. **GetMap**: Maps will be restricted based on your permissions:
   - Layers may be filtered out
   - Data may be clipped to your allowed area
   - Some styling options may be restricted

3. **GetFeatureInfo**: When clicking on the map:
   - You can only get information from layers you can access
   - Attributes may be filtered based on your permissions
   - Queries outside your allowed area may be denied

### Example WMS Workflow

1. **Request Capabilities**:
   ```
   https://example.com/geoserver/ows?service=WMS&version=1.3.0&request=GetCapabilities
   ```
   (Only authorized layers will appear in the response)

2. **Request a Map**:
   ```
   https://example.com/geoserver/ows?service=WMS&version=1.3.0&request=GetMap&layers=workspace:layer&styles=&bbox=-180,-90,180,90&width=768&height=384&srs=EPSG:4326&format=image/png
   ```
   (The map will be filtered based on your permissions)

## WFS - Web Feature Service

WFS is used for accessing and manipulating vector feature data.

### How ACL Affects WFS

1. **GetCapabilities**: Only feature types you have access to will be listed
2. **DescribeFeatureType**: You can only describe feature types you have access to
3. **GetFeature**: Feature data is filtered based on your permissions:
   - Features outside your allowed area may be filtered out
   - Attributes may be limited based on your permissions
   - Result sets may be limited in size

4. **Transaction** (WFS-T): Write operations are subject to additional restrictions:
   - Insert/Update/Delete may be denied entirely
   - Operations may be limited to specific areas
   - Only certain attributes may be editable

### Example WFS Workflow

1. **Request Capabilities**:
   ```
   https://example.com/geoserver/ows?service=WFS&version=2.0.0&request=GetCapabilities
   ```

2. **Request Features**:
   ```
   https://example.com/geoserver/ows?service=WFS&version=2.0.0&request=GetFeature&typeName=workspace:layer&outputFormat=application/json
   ```
   (Results will be filtered based on your permissions)

## WCS - Web Coverage Service

WCS is used for accessing raster coverage data.

### How ACL Affects WCS

1. **GetCapabilities**: Only coverages you have access to will be listed
2. **DescribeCoverage**: You can only describe coverages you have access to
3. **GetCoverage**: Coverage data is filtered based on your permissions:
   - Data may be clipped to your allowed area
   - Resolution may be limited based on your permissions
   - Format options may be restricted

### Example WCS Workflow

1. **Request Capabilities**:
   ```
   https://example.com/geoserver/ows?service=WCS&version=2.0.1&request=GetCapabilities
   ```

2. **Request Coverage**:
   ```
   https://example.com/geoserver/ows?service=WCS&version=2.0.1&request=GetCoverage&coverageId=workspace:coverage&format=image/tiff
   ```
   (Coverage will be filtered based on your permissions)

## WMTS - Web Map Tile Service

WMTS is used for accessing pre-rendered map tiles.

### How ACL Affects WMTS

1. **GetCapabilities**: Only layers you have access to will be listed
2. **GetTile**: Tiles will be served or denied based on your permissions:
   - Tiles outside your allowed area may be denied
   - Some zoom levels may be restricted

### Example WMTS Workflow

1. **Request Capabilities**:
   ```
   https://example.com/geoserver/gwc/service/wmts?request=GetCapabilities
   ```

2. **Request Tile**:
   ```
   https://example.com/geoserver/gwc/service/wmts?layer=workspace:layer&style=&tilematrixset=EPSG:4326&tilematrix=EPSG:4326:5&tilerow=12&tilecol=23&format=image/png
   ```
   (Tile access will be filtered based on your permissions)

## WPS - Web Processing Service

WPS is used for running geospatial processes on the server.

### How ACL Affects WPS

1. **GetCapabilities**: Only processes you have access to will be listed
2. **DescribeProcess**: You can only describe processes you have access to
3. **Execute**: Process execution is controlled based on your permissions:
   - Some processes may be entirely restricted
   - Input data may be filtered based on your permissions
   - Output formats may be limited

### Example WPS Workflow

1. **Request Capabilities**:
   ```
   https://example.com/geoserver/ows?service=WPS&version=1.0.0&request=GetCapabilities
   ```

2. **Execute Process**:
   ```
   https://example.com/geoserver/ows?service=WPS&version=1.0.0&request=Execute&identifier=JTS:buffer&datainputs=geom=...
   ```
   (Process execution will be controlled based on your permissions)

## CSW - Catalog Service for the Web

CSW is used for discovering and accessing metadata about geospatial data.

### How ACL Affects CSW

1. **GetCapabilities**: Service capabilities may be restricted based on your permissions
2. **GetRecords**: Metadata records are filtered based on your permissions:
   - You'll only see records for resources you have access to
   - Some metadata fields may be filtered

### Example CSW Workflow

1. **Request Capabilities**:
   ```
   https://example.com/geoserver/csw?service=CSW&version=2.0.2&request=GetCapabilities
   ```

2. **Search for Records**:
   ```
   https://example.com/geoserver/csw?service=CSW&version=2.0.2&request=GetRecords&typeNames=csw:Record&resultType=results
   ```
   (Results will be filtered based on your permissions)

## Handling Access Errors

When using OWS services, you may encounter access errors if you request resources or operations you don't have permission for:

- **403 Forbidden**: You don't have permission for the requested operation
- **Filtered Results**: You only receive the portion of data you have access to
- **Empty Results**: Your query may return no results if it only targets restricted areas
- **Service Exceptions**: XML/JSON error documents explaining the access issue

If you encounter persistent access issues, contact your GeoServer administrator to verify your permissions.

## Best Practices

To work effectively with OWS services under ACL restrictions:

1. **Always check capabilities first** to see what resources you have access to
2. **Limit requests to your known authorized areas** for better performance
3. **Handle potential access errors gracefully** in your client applications
4. **Use appropriate authentication** in all your requests
5. **Remember that different services may have different permission levels** (e.g., WMS view access but no WFS download access)