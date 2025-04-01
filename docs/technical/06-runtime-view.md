# Runtime View

This section describes the key runtime scenarios in GeoServer ACL, showing how the components interact at runtime to fulfill the main use cases.

## Authorization Process

The authorization process is the core workflow in GeoServer ACL. It begins when a user makes a request to GeoServer and ends with the user receiving a filtered response based on their access rights.

### Step-by-step explanation

1. **User Request**: A GIS client (web map, QGIS, etc.) sends an OWS request (e.g., WMS GetMap, WFS GetFeature) to GeoServer.

2. **Authorization Check**: GeoServer intercepts the request and delegates to the ACL Plugin to check authorization before processing.

3. **Plugin to API**: The ACL Plugin converts the GeoServer request to an ACL authorization request and sends it to the ACL API.

4. **API to Service**: The ACL API forwards the request to the ACL Service, which is the core business logic component.

5. **Rule Lookup**: The ACL Service queries the database for rules that match the request parameters (user, role, workspace, layer, etc.) and receives the matching rules.

6. **Access Decision**: The ACL Service evaluates the rules and determines the access decision (allow, deny, or limit), which is returned through the API to the Plugin.

7. **Apply Limits**: The ACL Plugin converts the access decision into GeoServer-specific access limits (e.g., layer filtering, attribute filtering, spatial filtering).

8. **Filtered Response**: GeoServer applies the access limits and returns a filtered response to the client, containing only the data the user is authorized to see.

## Rule Creation Process

The rule creation process is used by administrators to define access control rules through the GeoServer Admin UI.

### Step-by-step explanation

1. **Access UI**: An administrator accesses the ACL rules interface in the GeoServer Admin UI.

2. **Rule Interface**: GeoServer loads the rule management UI through the ACL Plugin.

3. **Rule Creation**: The administrator creates a new rule by filling out the form with user, role, workspace, layer, access type, and optionally limits.

4. **API Request**: The ACL Plugin converts the form data to an API request and sends it to the ACL API.

5. **Service Processing**: The ACL API forwards the request to the ACL Service for processing.

6. **Persistence**: The ACL Service validates the rule, assigns a priority, and persists it to the database, which confirms the save.

7. **Response**: The success response is returned through the API to the Plugin.

8. **UI Confirmation**: The Plugin updates the UI to show a confirmation message to the administrator.

## Layer Access Request Workflow

This scenario describes the detailed workflow when a user requests access to a specific layer through a WMS GetMap request.

### Step-by-step explanation

1. **Initial Request**: The user loads a map in QGIS that includes a protected layer.

2. **WMS Request**: QGIS sends a WMS GetMap request to GeoServer.

3. **Resource Access Manager**: GeoServer calls the `preFilter` method on the ACL Resource Access Manager.

4. **Build Request**: The Resource Access Manager uses the Access Request Builder to convert the GeoServer request to an ACL AccessRequest.

5. **Client to API**: The Client Adaptor sends the access request to the ACL API via a POST to the `/authorization` endpoint.

6. **Authorization Service**: The API forwards the request to the Authorization Service.

7. **Rule Retrieval**: The Authorization Service uses the Rule Management component to query the database for rules matching the request.

8. **Rule Evaluation**: The Authorization Service evaluates the rules based on priority and determines the final access decision.

9. **Result Return**: The access decision is returned through the API to the Client Adaptor and then to the Resource Access Manager.

10. **Apply Limits**: The Resource Access Manager converts the AccessInfo to GeoServer-specific limits, and GeoServer applies these limits (spatial filtering, attribute filtering).

11. **Response to User**: GeoServer sends the filtered response back to QGIS, which displays the filtered map to the user.

## Admin Rule Evaluation Workflow

This scenario describes how administrative permissions are evaluated when a user attempts to perform an administrative action on a workspace.

### Step-by-step explanation

1. **Admin Attempt**: A user attempts to perform an administrative action on a workspace through the GeoServer Admin UI.

2. **Admin Check**: GeoServer checks admin rights by calling the ACL Plugin.

3. **Request Authentication**: The Plugin sends an admin authorization request to the ACL API.

4. **Rule Query**: The Authorization Service queries the Admin Rule Management for matching admin rules.

5. **Rule Evaluation**: The Authorization Service evaluates the rules based on priority and determines if the user has administrative rights.

6. **Decision Return**: The admin access decision is returned through the API to the Plugin and then to GeoServer Core.

7. **UI Update**: Based on the decision, the UI either allows the administrative action or denies it.

These workflows illustrate the key runtime scenarios in GeoServer ACL, showing how the components interact to provide authorization services.