/* (c) 2023  Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.acl.autoconfigure.security;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Security configuration properties for the ACL service, bound to the
 * geoserver.acl.security prefix. Supports two authentication mechanisms that can be
 * enabled independently or together: internal (HTTP Basic with in-memory users) and
 * pre-authentication headers (trusted proxy).
 */
@Data
@ConfigurationProperties(value = SecurityConfigProperties.PREFIX)
public class SecurityConfigProperties {

    public static final String PREFIX = "geoserver.acl.security";

    /** Internal HTTP Basic authentication settings. */
    private Internal internal = new Internal();

    /** Pre-authentication header settings for trusted reverse-proxy deployments. */
    private PreauthHeaders headers = new PreauthHeaders();

    public boolean enabled() {
        return internal.isEnabled() || headers.isEnabled();
    }

    /**
     * Internal authentication configuration using HTTP Basic with an in-memory user store.
     * Users are defined as a map under geoserver.acl.security.internal.users.
     */
    public static @Data class Internal {
        /** Whether internal HTTP Basic authentication is enabled. */
        private boolean enabled;

        /**
         * Map of username to user info. Each entry defines credentials and role for an
         * internal API user. Example: acl.users.admin.password, acl.users.admin.admin.
         */
        private Map<String, UserInfo> users = Map.of();

        /**
         * Properties for an individual internal user.
         */
        @ToString(exclude = "password")
        public static @Data class UserInfo {
            /**
             * User password. Supports delegating encoder prefixes (e.g. {bcrypt}, {noop}).
             * Passwords without a prefix are treated as plaintext ({noop}).
             */
            private String password;

            /** Whether this user has administrator privileges (ROLE_ADMIN vs ROLE_USER). */
            private boolean admin;

            /** Whether this user account is enabled. Disabled users cannot authenticate. */
            private boolean enabled = true;

            public List<SimpleGrantedAuthority> authorities() {
                return Stream.of(admin ? "ROLE_ADMIN" : "ROLE_USER")
                        .map(SimpleGrantedAuthority::new)
                        .toList();
            }
        }
    }

    /**
     * Pre-authentication header configuration for deployments behind a trusted reverse proxy
     * (e.g. Spring Cloud Gateway). The proxy authenticates the user and forwards identity
     * via HTTP headers.
     */
    public static @Data class PreauthHeaders {
        /** Whether pre-authentication via headers is enabled. */
        private boolean enabled;

        /** HTTP header name carrying the authenticated username. */
        private String userHeader = "sec-username";

        /** HTTP header name carrying the comma-separated list of user roles. */
        private String rolesHeader = "sec-roles";

        /** Role names that grant administrator privileges. */
        private List<String> adminRoles = List.of("ADMIN");
    }
}
