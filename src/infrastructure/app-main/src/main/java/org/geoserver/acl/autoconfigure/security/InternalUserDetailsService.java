/* (c) 2026  Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.acl.autoconfigure.security;

import static org.springframework.util.StringUtils.hasText;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.geoserver.acl.autoconfigure.security.SecurityConfigProperties.Internal.UserInfo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * In-memory {@link org.springframework.security.core.userdetails.UserDetailsService} populated from
 * {@link SecurityConfigProperties} at startup. Converts each enabled
 * {@link SecurityConfigProperties.Internal.UserInfo} entry into a Spring Security {@link UserDetails}
 * with the configured authorities.
 *
 * <p>Passwords without an encoder prefix (e.g. {@code {bcrypt}}) are automatically prefixed with
 * {@code {noop}} for compatibility with
 * {@link org.springframework.security.crypto.factory.PasswordEncoderFactories#createDelegatingPasswordEncoder()}.
 *
 * @see InternalSecurityAutoConfiguration
 * @see CachingPasswordEncoder
 */
@Slf4j(topic = "org.geoserver.acl.autoconfigure.security")
class InternalUserDetailsService extends InMemoryUserDetailsManager {

    public InternalUserDetailsService(Map<String, UserInfo> users) {
        super(buildUserDetails(users));
    }

    private static Collection<UserDetails> buildUserDetails(Map<String, UserInfo> users) {
        List<UserDetails> userDetails = users.entrySet().stream()
                .filter(e -> e.getValue().isEnabled())
                .map(e -> toUserDetails(e.getKey(), e.getValue()))
                .toList();

        long enabledUsers = userDetails.stream().filter(UserDetails::isEnabled).count();
        if (0L == enabledUsers) {
            log.warn("No API users are enabled for HTTP Basic Auth. Loaded user names: {}", users.keySet());
        }
        return userDetails;
    }

    private static UserDetails toUserDetails(String username, SecurityConfigProperties.Internal.UserInfo userinfo) {
        log.info(
                "Loading internal user {}, admin: {}, enabled: {}", username, userinfo.isAdmin(), userinfo.isEnabled());
        validate(username, userinfo);
        return User.builder()
                .username(username)
                .password(ensurePrefixed(userinfo.getPassword()))
                .authorities(userinfo.authorities())
                .disabled(!userinfo.isEnabled())
                .build();
    }

    private static String ensurePrefixed(@NonNull String password) {
        if (!password.matches("(\\{.+\\}).+")) {
            return "{noop}%s".formatted(password);
        }

        return password;
    }

    private static void validate(final String name, SecurityConfigProperties.Internal.UserInfo info) {
        if (!hasText(name)) throw new IllegalArgumentException("User has no name: " + info);
        if (!hasText(info.getPassword())) throw new IllegalArgumentException("User %s has no password".formatted(name));
    }
}
