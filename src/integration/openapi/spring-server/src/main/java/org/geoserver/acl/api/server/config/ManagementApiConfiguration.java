/* (c) 2023  Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.acl.api.server.config;

import org.geoserver.acl.api.server.ManagementApiController;
import org.geoserver.acl.api.server.ManagementApiDelegate;
import org.geoserver.acl.api.server.management.ManagementApiImpl;
import org.geoserver.acl.management.ManagementService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({ApiObjectModelMappersConfiguration.class, JacksonObjectMapperConfiguration.class})
public class ManagementApiConfiguration {

    @Bean
    ManagementApiController managementApiController(ManagementApiDelegate delegate) {
        return new ManagementApiController(delegate);
    }

    @Bean
    ManagementApiDelegate managementApiDelegate(ManagementService service) {
        return new ManagementApiImpl(service);
    }
}
