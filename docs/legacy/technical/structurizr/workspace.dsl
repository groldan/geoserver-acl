workspace {
    name "GeoServer ACL"
    description "GeoServer Access Control List - Technical Architecture"

    model {
        user = person "GeoServer User" "End user accessing GeoServer resources"
        admin = person "GeoServer Admin" "Administrator configuring access rules"
        
        geoserverSystem = softwareSystem "GeoServer" "Geographic data server" {
            geoserverInstance = container "GeoServer Instance" "GeoServer application serving OGC services (WMS, WFS, etc.)" {
                aclPlugin = component "ACL Plugin" "Authorization plugin that enforces access rules"
                accessManager = component "ResourceAccessManager" "GeoServer component for data access control"
                
                aclPlugin -> accessManager "Integrates with"
            }
        }
        
        aclSystem = softwareSystem "GeoServer ACL" "Authorization system for GeoServer" {
            // Containers
            aclService = container "ACL Service" "Spring Boot application that manages and provides access rules" "Java, Spring Boot" {
                // Components
                rulesApi = component "Rules API" "REST API for managing rules" "Spring MVC, OpenAPI"
                authzApi = component "Authorization API" "API for authorization decisions" "Spring MVC, OpenAPI"
                adminRules = component "Admin Rules" "Admin rule management service" "Java"
                accessRules = component "Access Rules" "Access rule management service" "Java"
                authService = component "Authorization Service" "Core authorization logic" "Java"
                modelMapper = component "API Model Mapper" "Maps between domain and API models" "Java, MapStruct"
                persistence = component "Persistence" "Data access layer" "JPA, Spring Data"
            }
            
            database = container "Database" "Stores rule definitions" "PostgreSQL/H2" "Database"
        }
        
        // Relationships - External
        user -> geoserverSystem "Makes OGC service requests to"
        admin -> aclSystem "Manages access rules via"
        admin -> geoserverSystem "Configures"
        
        // Relationships - Between Systems
        geoserverInstance -> aclService "Requests authorization decisions from" "HTTP/REST"
        
        // Relationships - Internal Components
        aclPlugin -> authzApi "Requests authorization decisions from" "HTTP/REST"
        rulesApi -> adminRules "Uses"
        rulesApi -> accessRules "Uses"
        authzApi -> authService "Uses"
        adminRules -> persistence "Uses"
        accessRules -> persistence "Uses"
        authService -> adminRules "Uses"
        authService -> accessRules "Uses"
        modelMapper -> adminRules "Maps domain objects from/to"
        modelMapper -> accessRules "Maps domain objects from/to"
        persistence -> database "Reads from and writes to"
    }
    
    views {
        systemContext aclSystem "SystemContext" {
            include *
            autolayout lr
        }
        
        container aclSystem "Containers" {
            include *
            autolayout tb
        }
        
        component aclService "Components" {
            include *
            autolayout tb
        }
        
        dynamic aclSystem "AuthorizationFlow" "Flow of a typical authorization request" {
            user -> geoserverInstance "Makes OGC service request"
            geoserverInstance -> aclPlugin "Intercepts request"
            aclPlugin -> authzApi "Requests access decision"
            authzApi -> authService "Determines access"
            authService -> accessRules "Fetches applicable rules"
            authService -> adminRules "Fetches applicable admin rules"
            accessRules -> persistence "Reads rules"
            adminRules -> persistence "Reads rules"
            persistence -> database "Queries"
            authzApi -> aclPlugin "Returns access decision"
            geoserverInstance -> user "Returns filtered response"
            autoLayout
        }
        
        dynamic aclSystem "RuleManagementFlow" "Flow of rule management" {
            admin -> rulesApi "Creates/updates rules"
            rulesApi -> modelMapper "Maps API to domain objects"
            modelMapper -> accessRules "Passes domain objects"
            accessRules -> persistence "Persists rules"
            persistence -> database "Writes to"
            autoLayout
        }
        
        styles {
            element "Person" {
                shape Person
                background #08427B
                color #ffffff
            }
            element "GeoServer" {
                background #85BBF0
                color #000000
            }
            element "GeoServer ACL" {
                background #5CB85C
                color #ffffff
            }
            element "Database" {
                shape Cylinder
            }
        }
    }
}