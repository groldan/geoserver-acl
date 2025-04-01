# Migration from GeoFence

This page provides guidance on migrating from GeoFence to GeoServer ACL. It covers the similarities and differences between the two systems, migration strategies, and potential challenges.

## Overview

GeoServer ACL is a fork of GeoFence that has been modernized and enhanced while maintaining compatibility with the core rule-based authorization model. This makes migration between the two systems straightforward in many cases.

## Comparing GeoFence and GeoServer ACL

### Similarities

- **Rule-Based Authorization**: Both use a rule-based approach with priorities
- **Rule Structure**: Similar rule components (user, role, workspace, layer, etc.)
- **Admin Rules**: Both support workspace administration delegation
- **Integration Model**: Similar GeoServer plugin architecture

### Key Differences

| Feature | GeoFence | GeoServer ACL |
|---------|----------|---------------|
| **Java Version** | Java 8 | Java 11+ (Java 17 recommended) |
| **Framework** | Spring 4.x | Spring Boot 2.7+ |
| **API** | REST API | OpenAPI-defined REST API |
| **Database** | H2/MySQL/Oracle/PostgreSQL | PostgreSQL with PostGIS |
| **Architecture** | Monolithic | Microservice-ready |
| **Container Support** | Limited | Full Docker support |
| **Client Libraries** | Java only | Java, Python, JavaScript |
| **Caching** | Simple caching | Multi-level caching |
| **Event System** | Basic | Spring Cloud Bus integration |

## Migration Paths

There are several approaches to migrating from GeoFence to GeoServer ACL:

### 1. Database Migration

This approach involves exporting data from GeoFence and importing it into GeoServer ACL.

#### Step 1: Export GeoFence Rules

Export rules from GeoFence using its REST API:

```bash
curl -u admin:geoserver http://localhost:8080/geofence/rest/rules.json > geofence_rules.json
curl -u admin:geoserver http://localhost:8080/geofence/rest/adminrules.json > geofence_adminrules.json
```

#### Step 2: Convert Rule Format

The rule formats are similar but not identical. Use a script to convert between formats:

```python
import json
import sys

# Load GeoFence rules
with open('geofence_rules.json', 'r') as f:
    geofence_rules = json.load(f)

# Convert to GeoServer ACL format
acl_rules = []
for rule in geofence_rules['rules']:
    acl_rule = {
        "priority": rule['priority'],
        "access": rule['access'],
    }
    
    # Map user/role
    if rule['username'] != '*':
        acl_rule['username'] = rule['username']
    if rule['rolename'] != '*':
        acl_rule['rolename'] = rule['rolename']
    
    # Map resource
    if rule['service'] != '*':
        acl_rule['service'] = rule['service']
    if rule['request'] != '*':
        acl_rule['request'] = rule['request']
    if rule['workspace'] != '*':
        acl_rule['workspace'] = rule['workspace']
    if rule['layer'] != '*':
        acl_rule['layer'] = rule['layer']
    
    # Map limits and details
    if 'limits' in rule and rule['limits']:
        acl_rule['ruleLimits'] = {
            'allowedArea': rule['limits']['allowedArea'],
            'spatialFilterType': rule['limits']['spatialFilterType']
        }
    
    if 'layerDetails' in rule and rule['layerDetails']:
        acl_rule['layerDetails'] = {
            'defaultStyle': rule['layerDetails']['defaultStyle'],
            'catalogMode': rule['layerDetails']['catalogMode']
        }
        
        if 'allowedStyles' in rule['layerDetails']:
            acl_rule['layerDetails']['allowedStyles'] = rule['layerDetails']['allowedStyles']
        
        if 'attributes' in rule['layerDetails']:
            acl_rule['layerDetails']['attributes'] = {
                'accessType': rule['layerDetails']['attributes']['accessType']
            }
            
            if 'includedAttributes' in rule['layerDetails']['attributes']:
                acl_rule['layerDetails']['attributes']['includedAttributes'] = \
                    rule['layerDetails']['attributes']['includedAttributes']
            
            if 'excludedAttributes' in rule['layerDetails']['attributes']:
                acl_rule['layerDetails']['attributes']['excludedAttributes'] = \
                    rule['layerDetails']['attributes']['excludedAttributes']
    
    acl_rules.append(acl_rule)

# Save as ACL format
with open('acl_rules.json', 'w') as f:
    json.dump(acl_rules, f, indent=2)

print(f"Converted {len(acl_rules)} rules")
```

#### Step 3: Import to GeoServer ACL

Import the converted rules into GeoServer ACL:

```bash
curl -u admin:geoserver -X POST \
  http://localhost:8080/acl/api/rules/bulk \
  -H 'Content-Type: application/json' \
  -d @acl_rules.json

curl -u admin:geoserver -X POST \
  http://localhost:8080/acl/api/adminrules/bulk \
  -H 'Content-Type: application/json' \
  -d @acl_adminrules.json
```

### 2. Parallel Operation

Run GeoFence and GeoServer ACL in parallel during the transition:

1. Install GeoServer ACL alongside existing GeoFence
2. Migrate rules from GeoFence to GeoServer ACL
3. Configure a test GeoServer instance to use GeoServer ACL
4. Validate authorization behavior matches expectations
5. Gradually migrate production instances to GeoServer ACL
6. Decommission GeoFence once migration is complete

### 3. Direct Plugin Replacement

For simpler deployments, you can directly replace the GeoFence plugin with the GeoServer ACL plugin:

1. Backup your GeoFence rules
2. Remove GeoFence plugin JARs from GeoServer's lib directory
3. Add GeoServer ACL plugin JARs to the lib directory
4. Import your rules into GeoServer ACL
5. Restart GeoServer

## Configuration Migration

### GeoFence Server Configuration

GeoFence server configuration must be migrated to GeoServer ACL's application.yml:

| GeoFence Property | GeoServer ACL Property |
|-------------------|------------------------|
| `geofence-ovr.dbtype` | `spring.datasource.driver-class-name` |
| `geofence-ovr.dbname` | `spring.datasource.url` |
| `geofence-ovr.username` | `spring.datasource.username` |
| `geofence-ovr.password` | `spring.datasource.password` |
| `geofence-ovr.jndiName` | `spring.datasource.jndi-name` |
| `geofence-ovr.maxConnections` | `spring.datasource.hikari.maximum-pool-size` |
| `geofence-ovr.minConnections` | `spring.datasource.hikari.minimum-idle` |

### GeoFence Client Configuration

GeoFence client configuration in GeoServer must be updated:

| GeoFence Setting | GeoServer ACL Setting |
|------------------|------------------------|
| `ServicesUrl` | ACL Services URL |
| `InstanceName` | Instance Name |
| `AllowRemoteServices` | Allow Remote Services |
| `AllowDynamicStyles` | Not applicable |
| `UseRolesToFilter` | Not applicable |
| `AcceptedRoles` | Not applicable |

## Database Considerations

### Database Schema Differences

GeoServer ACL uses a slightly different database schema than GeoFence:

- Table names use a consistent naming convention
- Additional indices for performance
- PostGIS-specific spatial types
- No support for non-PostgreSQL databases in production

### Database Migration Script

If you need to directly migrate the database (advanced):

```sql
-- Create GeoServer ACL schema (simplified example)
CREATE TABLE acl_rules (
    id SERIAL PRIMARY KEY,
    priority INTEGER NOT NULL,
    username VARCHAR(255),
    rolename VARCHAR(255),
    workspace VARCHAR(255),
    layer VARCHAR(255),
    service VARCHAR(255),
    request VARCHAR(255),
    access VARCHAR(10) NOT NULL,
    -- Additional fields
);

-- Migrate rules from GeoFence to ACL
INSERT INTO acl_rules (priority, username, rolename, workspace, layer, service, request, access)
SELECT priority, 
       CASE WHEN username = '*' THEN NULL ELSE username END,
       CASE WHEN rolename = '*' THEN NULL ELSE rolename END,
       CASE WHEN workspace = '*' THEN NULL ELSE workspace END,
       CASE WHEN layer = '*' THEN NULL ELSE layer END,
       CASE WHEN service = '*' THEN NULL ELSE service END,
       CASE WHEN request = '*' THEN NULL ELSE request END,
       access
FROM geofence_rules;

-- Similar migrations for other tables
```

## Testing the Migration

After migrating, it's essential to test that authorization works the same way:

1. **Rule Comparison**: Compare rule counts and properties
2. **Authorization Testing**: Test the same requests against both systems
3. **Performance Testing**: Compare response times
4. **Edge Case Testing**: Test complex rules and special cases

## Common Migration Challenges

### Challenge: Rule Priority Conflicts

**Problem**: Different rule priorities may result in different evaluation order.

**Solution**: Verify and adjust rule priorities after migration.

### Challenge: Database Connection Issues

**Problem**: GeoServer ACL is more strict about database connection parameters.

**Solution**: Ensure PostgreSQL connection settings are correct and the database has PostGIS installed.

### Challenge: API Compatibility

**Problem**: Scripts or applications using the GeoFence API will need updates.

**Solution**: Update integrations to use the new OpenAPI-based endpoints or use the provided client libraries.

### Challenge: Authentication Integration

**Problem**: Authentication configuration might need adjustment.

**Solution**: Review and update authentication settings in GeoServer ACL's security configuration.

## Post-Migration Optimization

After successful migration, consider these optimizations:

1. **Rule Optimization**: Review and consolidate rules
2. **Caching Configuration**: Tune cache settings for your workload
3. **Performance Tuning**: Optimize database and JVM settings
4. **Monitoring Setup**: Configure monitoring for the new system

## Rollback Plan

In case of migration issues, prepare a rollback plan:

1. Keep GeoFence installation available during migration
2. Maintain backups of all GeoFence rules and configurations
3. Document the exact steps to revert to GeoFence if needed
4. Test the rollback procedure before starting the migration

## Migration Checklist

- [ ] Backup all GeoFence rules and configurations
- [ ] Export rules from GeoFence 
- [ ] Install GeoServer ACL
- [ ] Convert and import rules
- [ ] Update GeoServer plugin configuration
- [ ] Test authorization behavior
- [ ] Update scripts and integrations
- [ ] Monitor performance and behavior
- [ ] Decommission GeoFence after successful validation

## Additional Resources

- [GeoFence Documentation](https://docs.geoserver.org/latest/en/user/community/geofence/index.html)
- [GeoServer ACL API Reference](../api/index.md)
- [GeoServer ACL Installation Guide](installation.md)