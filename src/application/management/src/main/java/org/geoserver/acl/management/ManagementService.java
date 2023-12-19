/* (c) 2023  Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.acl.management;

import java.util.Objects;
import java.util.Set;

public class ManagementService {

    public Set<String> renameLayerRules(
            String oldWorkspace, String oldLayerName, String newWorkspace, String newLayerName) {

        if (Objects.equals(oldWorkspace, newWorkspace)
                && Objects.equals(oldLayerName, newLayerName)) {
            return Set.of();
        }

        throw new UnsupportedOperationException();
    }

    public WorkspaceRenameResult renameWorkspace(String oldWorkspace, String newWorkspace) {
        throw new UnsupportedOperationException();
    }
}
