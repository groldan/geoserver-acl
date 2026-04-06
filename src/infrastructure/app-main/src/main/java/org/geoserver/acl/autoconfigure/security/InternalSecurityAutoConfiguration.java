/* (c) 2023  Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.acl.autoconfigure.security;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@AutoConfiguration
@ConditionalOnInternalAuthenticationEnabled
@EnableConfigurationProperties(SecurityConfigProperties.class)
@Slf4j(topic = "org.geoserver.acl.autoconfigure.security")
public class InternalSecurityAutoConfiguration {

    @Bean
    AuthenticationProvider internalAuthenticationProvider(
            @Qualifier("internalUserDetailsService") UserDetailsService internalUserDetailsService,
            PasswordEncoder encoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(internalUserDetailsService);
        provider.setAuthoritiesMapper(new NullAuthoritiesMapper());
        provider.setPasswordEncoder(new CachingPasswordEncoder(encoder));

        return provider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean("internalUserDetailsService")
    UserDetailsService internalUserDetailsService(SecurityConfigProperties config) {

        Map<String, SecurityConfigProperties.Internal.UserInfo> users =
                config.getInternal().getUsers();
        return new InternalUserDetailsService(users);
    }
}
