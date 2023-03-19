package org.geoserver.cloud.acl.autoconfigure.springdoc;

import org.geoserver.cloud.acl.config.springdoc.SpringDocHomeRedirectConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@ConditionalOnProperty(
        name = "springdoc.swagger-ui.enabled",
        havingValue = "true",
        matchIfMissing = true)
@Import(SpringDocHomeRedirectConfiguration.class)
public class SpringDocHomeRedirectAutoConfiguration {}