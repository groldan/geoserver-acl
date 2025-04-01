# Environmental Protection Agency Data Sharing Portal

## Client Profile

A State Environmental Protection Agency responsible for monitoring, protecting, and reporting on environmental conditions needed to share data with various stakeholders while maintaining strict control over sensitive information.

## Challenge

The agency needed to provide access to environmental monitoring data for multiple stakeholders with different access requirements:

- Public citizens looking for general environmental information
- Other government agencies requiring detailed but restricted data
- Academic researchers needing specific datasets for studies
- Commercial entities using environmental data for business purposes
- Agency staff requiring full access with editing capabilities

Key challenges included:

- **Varied Data Sensitivity**: Some data could be publicly shared while other datasets contained sensitive information
- **Multi-Level Access**: Different stakeholders needed different levels of detail from the same datasets
- **Attribute Protection**: Certain attributes within datasets needed to be masked for some users
- **Spatial Restrictions**: High-resolution data for sensitive areas required protection
- **Performance Requirements**: Large datasets needed to be served efficiently despite access controls
- **Regulatory Compliance**: Data sharing had to comply with various regulations and interagency agreements

## Solution

GeoServer ACL was deployed with a sophisticated rule structure to meet these complex requirements:

### Access Control Architecture

![Access Control Architecture](../assets/images/epa_architecture.svg)

### Key Implementation Details

1. **Audience-Based Access Levels**
   - Public users: Access to non-sensitive data without authentication
   - Government agencies: Tiered access based on interagency agreements
   - Academic researchers: Access to specific datasets with sensitive attributes filtered
   - Commercial users: Rate-limited access to public data only
   - Agency staff: Full access based on departmental responsibilities

2. **Attribute-Level Security**
   - Sensitive attributes (e.g., exact locations of endangered species) were filtered
   - Critical infrastructure details were masked from unauthorized users
   - Personal identification information was removed from public data

3. **Spatial Filtering**
   - High-resolution data for sensitive ecological areas was downsampled for public view
   - Precise locations for certain features were offset or generalized for public users
   - Boundaries for sensitive areas were simplified in public views

4. **Service Controls**
   - WMS: Available to all users with appropriate layer restrictions
   - WFS: Limited to authenticated users with appropriate download permissions
   - WCS: Restricted to agency staff and approved researchers
   - WPS: Available only to internal agency users

### Rule Configuration Example

```json
{
  "priority": 500,
  "role": "RESEARCHER",
  "workspace": "water_quality",
  "layer": "*",
  "access": "LIMIT",
  "layerDetails": {
    "allowedStyles": ["scientific", "standard"],
    "attributes": {
      "includedAttributes": ["sample_date", "ph_level", "temperature", "dissolved_oxygen"],
      "accessType": "READONLY"
    },
    "catalogMode": "MIXED"
  }
}
```

## Results

The implementation of GeoServer ACL delivered multiple benefits:

- **Expanded Data Sharing**: Successfully shared most data with the public while protecting sensitive information
- **Streamlined Access**: Reduced data request processing time from weeks to instant self-service
- **Enhanced Collaboration**: Enabled cross-department collaboration without compromising regulatory restrictions
- **Regulatory Compliance**: Granular access control supported compliance with data privacy regulations
- **Improved Response Time**: Ability to quickly respond to emerging environmental incidents by adjusting permissions temporally
- **Maintained Performance**: Sustained system performance even with complex rule evaluations

## Key Takeaways

The implementation demonstrated several key lessons:

1. **Graduated Access**: Creating tiered access levels allowed maximizing data sharing while protecting sensitive information
2. **Attribute-Level Controls**: The ability to filter specific attributes was critical for balancing transparency with security
3. **Service-Based Rules**: Different services (WMS, WFS, WCS) required different permission models
4. **Dynamic Permissions**: The ability to adjust permissions quickly during environmental incidents proved invaluable
5. **Performance Tuning**: Caching strategies were essential for maintaining performance with complex permission rules

## Testimonial

> "GeoServer ACL gave us the confidence to open our data portal to a wider audience. We can now share critical environmental information with the public and partner agencies while maintaining precise control over sensitive data access."
> 
> *— Dr. Marcus Johnson, Environmental Data Program Director*