/* (c) 2023  Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.acl.management;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;

import java.util.Set;

@Value
@Builder
public class WorkspaceRenameResult {

    @Default private Set<String> dataRules = Set.of();
    @Default private Set<String> adminRules = Set.of();
}
