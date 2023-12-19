/* (c) 2023  Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.acl.api.server.management;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.geoserver.acl.api.model.RenameLayerRequest;
import org.geoserver.acl.api.model.RenameResponse;
import org.geoserver.acl.api.model.RenameWorkspaceRequest;
import org.geoserver.acl.api.server.ManagementApiDelegate;
import org.geoserver.acl.api.server.support.IsAdmin;
import org.geoserver.acl.management.ManagementService;
import org.geoserver.acl.management.WorkspaceRenameResult;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import javax.annotation.Nullable;

@RequiredArgsConstructor
@IsAdmin
public class ManagementApiImpl implements ManagementApiDelegate {

    @NonNull private final ManagementService service;

    /** {@inheritDoc} */
    @Override
    public ResponseEntity<RenameResponse> renameWorkspace(RenameWorkspaceRequest request) {

        String oldWorkspace = request.getOldName();
        String newWorkspace = request.getNewName();

        WorkspaceRenameResult result = service.renameWorkspace(oldWorkspace, newWorkspace);

        RenameResponse response = new RenameResponse();
        result.getAdminRules().forEach(response::addAdminRulesItem);
        result.getDataRules().forEach(response::addAdminRulesItem);

        return ResponseEntity.ok(response);
    }

    /** {@inheritDoc} */
    @Override
    public ResponseEntity<RenameResponse> renameLayer(RenameLayerRequest request) {

        @Nullable String oldWorkspace = request.getOldWorkspace();
        @Nullable String newWorkspace = request.getNewWorkspace();

        String oldLayerName = request.getOldName();
        String newLayerName = request.getNewName();

        Set<String> result =
                service.renameLayerRules(oldWorkspace, oldLayerName, newWorkspace, newLayerName);

        RenameResponse response = new RenameResponse();
        result.forEach(response::addAdminRulesItem);

        return ResponseEntity.ok(response);
    }
}
