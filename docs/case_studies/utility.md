# Utility Company Asset Management

## Client Profile

A major European utility provider managing electricity, gas, and water infrastructure across multiple countries needed a secure system for managing access to their critical infrastructure data by both internal staff and external contractors.

## Challenge

The utility company faced several complex challenges in managing access to their critical infrastructure data:

- **Asset Diversity**: Managing different types of assets (electricity, gas, water, telecommunications) with varied security requirements
- **External Collaboration**: Working with contractors who needed temporary access to specific assets and areas
- **Regulatory Requirements**: Complying with critical infrastructure protection regulations across multiple countries
- **Emergency Response**: Enabling rapid access changes during incidents while maintaining security
- **Department Silos**: Breaking down data silos between departments while respecting access boundaries
- **Geographic Scope**: Operating across multiple countries with different regional teams

Key concerns included protecting sensitive infrastructure data from unauthorized access while enabling efficient operations and maintenance activities.

## Solution

GeoServer ACL was implemented with a comprehensive security model:

### Access Control Architecture

![Access Control Architecture](../assets/images/utility_architecture.svg)

### Key Implementation Details

1. **Department-Based Access Control**
   - Electricity, gas, water, and telecommunications departments each received access to relevant asset types
   - Cross-department access was granted based on specific project needs
   - Management layers received appropriate visibility across departments

2. **Contractor Management**
   - Contractors received access restricted to active project areas
   - Time-based expiration automatically revoked access at project completion
   - Workspace and layer restrictions limited access to only relevant data
   - Attribute filtering prevented access to sensitive asset information

3. **Spatial Access Control**
   - Geographic boundaries restricted view and edit capabilities
   - Regional teams were limited to their operational areas
   - Emergency response teams received broader geographic access

4. **Critical Infrastructure Protection**
   - Attribute-level security protected sensitive information about critical infrastructure
   - Detailed technical specifications were only visible to authorized personnel
   - Capacity and vulnerability data were highly restricted

5. **Identity and Authentication**
   - Integration with the company's identity management system
   - Role-based access control aligned with organizational structure
   - Multi-factor authentication for sensitive operations

6. **Emergency Response Override**
   - Hierarchical rule structure allowing emergency response teams to override normal restrictions
   - Temporary access elevation during incidents with automatic expiration
   - Comprehensive audit logging of all emergency access

### Rule Configuration Example

```json
{
  "priority": 300,
  "role": "CONTRACTOR",
  "workspace": "electricity",
  "layer": "*",
  "access": "LIMIT",
  "layerDetails": {
    "allowedStyles": ["maintenance", "standard"],
    "attributes": {
      "excludedAttributes": ["capacity", "vulnerability_index", "technical_specs"],
      "accessType": "READONLY"
    }
  },
  "ruleLimits": {
    "allowedArea": "POLYGON((4.1 51.5, 4.2 51.5, 4.2 51.6, 4.1 51.6, 4.1 51.5))",
    "spatialFilterType": "CLIP"
  }
}
```

## Results

The implementation of GeoServer ACL delivered multiple benefits:

- **Enhanced Collaboration**: Streamlined collaboration between internal teams and external contractors
- **Security Improvement**: Improved security posture with precise control over sensitive infrastructure data
- **Administrative Efficiency**: Reduced administrative overhead through automated, role-based permission management
- **Regulatory Compliance**: Enhanced compliance with critical infrastructure protection regulations
- **Audit Capability**: Simplified auditing of data access and modifications
- **Emergency Readiness**: Improved ability to rapidly adjust permissions during emergency situations

## Key Takeaways

The implementation demonstrated several key lessons:

1. **Balanced Security**: Finding the balance between protection and operational efficiency was crucial
2. **Time-Based Access**: Temporal controls for contractor access proved essential for security
3. **Hierarchy of Rules**: Creating a clear priority structure for rules enabled emergency override scenarios
4. **Integration Focus**: Seamless integration with existing identity systems drove adoption
5. **Attribute Protection**: Protecting specific attributes within layers was critical for securing sensitive information

## Testimonial

> "GeoServer ACL has revolutionized how we manage access to our infrastructure data. We've eliminated our previous security concerns while making it easier for authorized users to access exactly the information they need."
> 
> *— Head of GIS Infrastructure, Utility Provider*