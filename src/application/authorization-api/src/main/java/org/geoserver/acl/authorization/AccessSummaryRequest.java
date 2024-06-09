/* (c) 2024  Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.acl.authorization;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.Set;

/**
 * Represents the converged set of visible layer names of a specific workspace for for a {@link
 * AccessSummaryRequest}
 *
 * @since 2.3
 * @see WorkspaceAccessSummary
 */
@Value
@Builder(builderClassName = "Builder")
public class AccessSummaryRequest {

    /**
     * The authentication user name on behalf of which the reques is performed, {@code null} only if
     * the request is anonymous, and hence {@link #roles} would contain some role name with
     * anonymous meaning (usually {@literal ROLE_ANONYMOUS}).
     */
    private String user;

    /** The authentication role names on behalf of which the request is performed. */
    @NonNull private Set<String> roles;

    public static class Builder {
        // Ignore squid:S1068, private field required for the lombok-generated build() method
        @SuppressWarnings({"unused", "squid:S1068"})
        private Set<String> roles = Set.of();

        public Builder roles(String... roleNames) {
            if (null == roleNames) return roles(Set.of());
            return roles(Set.of(roleNames));
        }

        public Builder roles(Set<String> roleNames) {
            if (null == roleNames) {
                this.roles = Set.of();
                return this;
            }
            this.roles = Set.copyOf(roleNames);
            return this;
        }
    }
}
