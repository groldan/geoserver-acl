# Spatial & Attribute Filtering

This page explains how to configure fine-grained access control in GeoServer ACL using spatial and attribute filtering.

## Introduction to Advanced Filtering

GeoServer ACL goes beyond simple allow/deny rules by providing advanced filtering capabilities:

1. **Spatial Filtering**: Restrict access to specific geographic areas
2. **Attribute Filtering**: Control which attributes (columns) users can see or edit
3. **CRS Restrictions**: Limit which coordinate reference systems can be used
4. **Layer Mode**: Control how restrictions are applied and presented to users

These filtering options allow for precise control over data access while maintaining security and privacy requirements.

## Spatial Filtering

Spatial filtering restricts users to viewing or editing data within specific geographic areas.

### Configuring Spatial Limits

To apply spatial restrictions to a rule:

1. Create or edit a rule with `access` set to `LIMIT`
2. Add a `ruleLimits` section with an `allowedArea` geometry
3. Set the `spatialFilterType` to determine how filtering is applied

Example rule with spatial filtering:

```json
{
  "priority": 100,
  "access": "LIMIT",
  "username": "field_worker",
  "workspace": "citydata",
  "layer": "buildings",
  "ruleLimits": {
    "allowedArea": "POLYGON((10.0 43.0, 10.0 44.0, 11.0 44.0, 11.0 43.0, 10.0 43.0))",
    "spatialFilterType": "INTERSECT"
  }
}
```

### Spatial Filter Types

GeoServer ACL supports two types of spatial filtering:

1. **INTERSECT**: Returns features that intersect with the allowed area (default)
   - Features partially inside the allowed area are included
   - Only the portions within the allowed area may be shown (depending on the visualization)

2. **CLIP**: Clips features to the boundary of the allowed area
   - Features are cut at the boundary of the allowed area
   - Only the portions within the allowed area are returned
   - More processing-intensive but provides stricter control

![Spatial Filtering Types](../assets/images/spatial_restriction.png)

### Drawing Allowed Areas

You can specify allowed areas in several ways:

1. **Using the Admin UI**:
   - Draw the area on an interactive map
   - Paste a WKT (Well-Known Text) geometry

2. **Using the REST API**:
   - Provide a WKT geometry in the `allowedArea` field
   - Use a GeoJSON object converted to WKT

### Supported Geometry Types

The `allowedArea` field supports various geometry types:

- **POLYGON**: For simple area restrictions
- **MULTIPOLYGON**: For disconnected allowed areas
- **GEOMETRYCOLLECTION**: For complex geometries

### Best Practices for Spatial Filtering

1. **Simplify Complex Geometries**: Very detailed geometries can impact performance
2. **Use Appropriate Coordinate Reference Systems**: Make sure your geometries match your data's CRS
3. **Consider Buffer Zones**: Add buffer zones around areas if exact boundaries aren't critical
4. **Set Appropriate Priorities**: Ensure spatial rules have appropriate priorities relative to other rules

## Attribute Filtering

Attribute filtering controls which attributes (data columns) users can access or modify.

### Configuring Attribute Restrictions

To apply attribute filtering:

1. Create or edit a rule with `access` set to `LIMIT`
2. Add a `layerDetails` section with an `attributes` configuration
3. Specify attributes to include or exclude
4. Set the access type for the attributes

Example rule with attribute filtering:

```json
{
  "priority": 100,
  "access": "LIMIT",
  "username": "analyst",
  "workspace": "citydata",
  "layer": "population",
  "layerDetails": {
    "attributes": {
      "includedAttributes": [
        "district_name",
        "population_count",
        "area_sqkm"
      ],
      "accessType": "READONLY"
    }
  }
}
```

### Attribute Access Types

GeoServer ACL supports different access levels for attributes:

1. **READONLY**: Users can view but not modify the specified attributes
2. **READWRITE**: Users can both view and modify the specified attributes

### Include vs. Exclude Mode

You can control attributes in two ways:

1. **Include Mode**: List explicitly which attributes are accessible
   ```json
   "includedAttributes": [
     "attribute1", 
     "attribute2"
   ]
   ```

2. **Exclude Mode**: List which attributes should be hidden
   ```json
   "excludedAttributes": [
     "sensitive_attribute1", 
     "sensitive_attribute2"
   ]
   ```

### Special Attribute Handling

Some special considerations for attribute filtering:

1. **Geometry Attributes**: Spatial attributes can be filtered like other attributes
2. **Required Attributes**: Some operations may require specific attributes to function
3. **Default Attributes**: If no attributes are specified, all attributes are accessible

## Combining Spatial and Attribute Filtering

For maximum control, you can combine spatial and attribute filtering in a single rule:

```json
{
  "priority": 100,
  "access": "LIMIT",
  "username": "field_worker",
  "workspace": "citydata",
  "layer": "buildings",
  "ruleLimits": {
    "allowedArea": "POLYGON((10.0 43.0, 10.0 44.0, 11.0 44.0, 11.0 43.0, 10.0 43.0))",
    "spatialFilterType": "INTERSECT"
  },
  "layerDetails": {
    "attributes": {
      "includedAttributes": [
        "building_id",
        "building_type",
        "height",
        "geometry"
      ],
      "accessType": "READONLY"
    }
  }
}
```

## CRS Restrictions

You can limit which Coordinate Reference Systems (CRS) can be used:

```json
{
  "priority": 100,
  "access": "LIMIT",
  "username": "analyst",
  "workspace": "citydata",
  "layer": "buildings",
  "ruleLimits": {
    "allowedCRS": [
      "EPSG:4326",
      "EPSG:3857"
    ]
  }
}
```

## Catalog Mode

Catalog Mode controls how GeoServer handles unauthorized access to layers:

```json
{
  "priority": 100,
  "access": "LIMIT",
  "username": "public",
  "workspace": "citydata",
  "layer": "*",
  "layerDetails": {
    "catalogMode": "MIXED"
  }
}
```

### Catalog Mode Options

1. **HIDE**: Unauthorized layers are completely hidden from capabilities documents
   - Most secure option
   - Users won't even know the layer exists

2. **CHALLENGE**: Unauthorized layers appear in capabilities but generate authorization errors when accessed
   - Shows all available layers
   - Requires authentication to access restricted layers
   - May lead to user confusion due to access errors

3. **MIXED**: Layers appear in capabilities, and requests return only authorized data
   - Shows all available layers
   - Returns partial results based on authorizations
   - Provides a more seamless user experience

## Allowed Styles

You can control which styles can be applied to a layer:

```json
{
  "priority": 100,
  "access": "LIMIT",
  "username": "analyst",
  "workspace": "citydata",
  "layer": "buildings",
  "layerDetails": {
    "allowedStyles": [
      "default",
      "highlight",
      "thematic"
    ]
  }
}
```

## Advanced Use Cases

### Multi-Level Security

For organizations with classified data:

```json
{
  "priority": 100,
  "access": "LIMIT",
  "rolename": "LEVEL_SECRET",
  "workspace": "classified",
  "layer": "infrastructure",
  "layerDetails": {
    "attributes": {
      "excludedAttributes": [
        "top_secret_info"
      ],
      "accessType": "READONLY"
    }
  },
  "ruleLimits": {
    "allowedArea": "POLYGON((...country boundary...))",
    "spatialFilterType": "CLIP"
  }
}
```

### Field Data Collection

For field workers collecting data in assigned areas:

```json
{
  "priority": 100,
  "access": "LIMIT",
  "username": "field_worker_123",
  "workspace": "survey",
  "layer": "observations",
  "layerDetails": {
    "attributes": {
      "includedAttributes": [
        "observation_id",
        "observation_type",
        "comments",
        "status",
        "geometry"
      ],
      "accessType": "READWRITE"
    }
  },
  "ruleLimits": {
    "allowedArea": "POLYGON((...assigned area...))",
    "spatialFilterType": "INTERSECT"
  }
}
```

### Public Data Portal with Privacy Filtering

For public access to data with personal information removed:

```json
{
  "priority": 100,
  "access": "LIMIT",
  "rolename": "ROLE_ANONYMOUS",
  "workspace": "public",
  "layer": "health_statistics",
  "layerDetails": {
    "attributes": {
      "excludedAttributes": [
        "personal_id",
        "address",
        "phone_number",
        "email"
      ],
      "accessType": "READONLY"
    }
  },
  "layerDetails": {
    "catalogMode": "MIXED"
  }
}
```

## Testing and Verification

After configuring spatial and attribute filters, you should test them thoroughly:

1. **Test Spatial Restrictions**:
   - Request data outside the allowed area and verify it's filtered out
   - Request data that crosses boundaries and check behavior with INTERSECT vs. CLIP

2. **Test Attribute Restrictions**:
   - Request all attributes and verify restricted ones are filtered
   - Test editing capabilities match the configured access types

3. **Test Combined Restrictions**:
   - Verify that both spatial and attribute restrictions work together
   - Test edge cases and boundary conditions

## Performance Considerations

Advanced filtering can impact performance:

1. **Spatial Filtering Impact**:
   - CLIP operations are more CPU-intensive than INTERSECT
   - Complex geometries in allowed areas increase processing time
   - Consider simplifying geometries for better performance

2. **Attribute Filtering Impact**:
   - Filtering attributes has minimal performance impact
   - Including only necessary attributes can improve response times
   - Exclude mode may be more efficient with many attributes

3. **Optimization Strategies**:
   - Use spatial database indexes on geometry columns
   - Cache frequently requested filtered views
   - Monitor query performance and adjust filters as needed