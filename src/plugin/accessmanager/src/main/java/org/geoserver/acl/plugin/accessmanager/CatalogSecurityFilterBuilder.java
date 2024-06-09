/* (c) 2024 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 *
 * Original from GeoServer 2.24-SNAPSHOT under GPL 2.0 license
 */
package org.geoserver.acl.plugin.accessmanager;

import static org.geoserver.catalog.Predicates.acceptAll;
import static org.geoserver.catalog.Predicates.and;
import static org.geoserver.catalog.Predicates.equal;
import static org.geoserver.catalog.Predicates.in;
import static org.geoserver.catalog.Predicates.isInstanceOf;
import static org.geoserver.catalog.Predicates.isNull;
import static org.geoserver.catalog.Predicates.or;

import org.geoserver.acl.authorization.AccessSummary;
import org.geoserver.acl.authorization.WorkspaceAccessSummary;
import org.geoserver.catalog.CatalogInfo;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.PublishedInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geotools.api.filter.Filter;
import org.springframework.lang.NonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Gabriel Roldan - Camptocamp
 */
public class CatalogSecurityFilterBuilder {

    private final AccessSummary viewables;

    public CatalogSecurityFilterBuilder(AccessSummary viewables) {
        this.viewables = Objects.requireNonNull(viewables);
    }

    @SuppressWarnings("unchecked")
    public Filter build(Class<? extends CatalogInfo> clazz) {
        Objects.requireNonNull(clazz);

        if (WorkspaceInfo.class.isAssignableFrom(clazz)) {
            return workspaceNameFilter("name");
        }
        if (NamespaceInfo.class.isAssignableFrom(clazz)) {
            return workspaceNameFilter("prefix");
        }
        if (StoreInfo.class.isAssignableFrom(clazz)) {
            return workspaceNameFilter("workspace.name");
        }
        if (ResourceInfo.class.isAssignableFrom(clazz)) {
            return layerFilter("store.workspace.name", "name");
        }
        if (PublishedInfo.class.isAssignableFrom(clazz)) {
            return publishedInfoFilter((Class<? extends PublishedInfo>) clazz);
        }
        if (StyleInfo.class.isAssignableFrom(clazz)) {
            return styleFilter();
        }
        throw new UnsupportedOperationException(
                "Unknown CatalogInfo type: " + clazz.getCanonicalName());
    }

    private Filter styleFilter() {
        return workspaceNameFilter("workspace.name");
    }

    private Filter publishedInfoFilter(Class<? extends PublishedInfo> clazz) {
        if (LayerInfo.class.isAssignableFrom(clazz)) {
            return layerFilter("resource.store.workspace.name", "name");
        }
        if (LayerGroupInfo.class.isAssignableFrom(clazz)) {
            return layerFilter("workspace.name", "name");
        }
        Filter layerFilter = and(isInstanceOf(LayerInfo.class), build(LayerInfo.class));
        Filter groupFilter = and(isInstanceOf(LayerGroupInfo.class), build(LayerGroupInfo.class));
        return or(layerFilter, groupFilter);
    }

    private Filter layerFilter(String workspaceProperty, String nameProperty) {
        List<WorkspaceAccessSummary> summaries = viewables.getWorkspaces();

        Filter filter = acceptAll();
        for (WorkspaceAccessSummary wsSummary : summaries) {
            Filter wsLayersFitler =
                    workspaceLayersFilter(wsSummary, workspaceProperty, nameProperty);
            filter = Filter.INCLUDE.equals(filter) ? wsLayersFitler : or(filter, wsLayersFitler);
        }
        return filter;
    }

    @NonNull
    private Filter workspaceLayersFilter(
            WorkspaceAccessSummary vl, String workspaceProperty, String nameProperty) {

        String workspace = vl.getWorkspace();
        // TODO: negate forbidden ones
        Set<String> allowed = vl.getAllowed();
        Set<String> forbidden = vl.getForbidden();

        Filter workspaceFilter = workspaceNameFilter(workspaceProperty, Set.of(workspace));
        if (allowed.contains(WorkspaceAccessSummary.ANY)) {
            return workspaceFilter;
        }
        Filter layerNamesFilter;
        if (allowed.size() == 1) {
            layerNamesFilter = equal(nameProperty, allowed.iterator().next());
        } else {
            layerNamesFilter = in(nameProperty, List.copyOf(allowed));
        }
        return and(workspaceFilter, layerNamesFilter);
    }

    private Set<String> getVisibleWorkspaces() {
        return viewables.visibleWorkspaces();
    }

    private Filter workspaceNameFilter(String workspaceProperty) {
        Set<String> visibleWorkspaces = getVisibleWorkspaces();
        return workspaceNameFilter(workspaceProperty, visibleWorkspaces);
    }

    private Filter workspaceNameFilter(String workspaceProperty, Set<String> visibleWorkspaces) {
        if (visibleWorkspaces.contains("*")) {
            return acceptAll();
        }
        Filter filter = acceptAll();
        if (visibleWorkspaces.contains("")) {
            filter = isNull(workspaceProperty);
            visibleWorkspaces = new HashSet<>(visibleWorkspaces);
            visibleWorkspaces.remove("");
        }
        if (!visibleWorkspaces.isEmpty()) {
            List<String> workspaces = List.copyOf(visibleWorkspaces);
            Filter namesFilter;
            if (workspaces.size() == 1) {
                namesFilter = equal(workspaceProperty, workspaces.get(0));
            } else {
                namesFilter = in(workspaceProperty, workspaces);
            }
            filter = Filter.INCLUDE.equals(filter) ? namesFilter : or(filter, namesFilter);
        }
        return filter;
    }
}
