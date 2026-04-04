/* (c) 2024  Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.acl.autoconfigure.messaging.bus;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.geoserver.acl.messaging.bus.RemoteAclRuleEventsBridge;
import org.geoserver.acl.messaging.bus.RemoteRuleEvent;
import org.springframework.boot.amqp.autoconfigure.RabbitAutoConfiguration;
import org.springframework.boot.amqp.autoconfigure.RabbitProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.bus.BusAutoConfiguration;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.cloud.bus.ConditionalOnBusEnabled;
import org.springframework.cloud.bus.ServiceMatcher;
import org.springframework.cloud.bus.event.Destination;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * Auto-configuration for Spring Cloud Bus integration, enabling distributed ACL rule change events
 * over RabbitMQ.
 *
 * <p>Activates only when <b>both</b> {@code geoserver.bus.enabled=true} and {@code
 * spring.cloud.bus.enabled=true} (via {@link ConditionalOnBusEnabled}). By default, both are
 * {@code false} in {@code application.yml}, so no RabbitMQ connection is attempted.
 *
 * <p>Because {@code spring-cloud-starter-bus-amqp} is on the classpath, Spring Boot's {@link
 * RabbitAutoConfiguration} would normally activate unconditionally (it only checks for class
 * presence). To prevent unwanted connection attempts when the bus is disabled, {@link
 * RabbitAutoConfiguration} is <b>excluded</b> via {@code spring.autoconfigure.exclude} in {@code
 * application.yml} and explicitly {@link Import @Import}ed here so it is only loaded when this
 * configuration's conditions are met.
 *
 * @since 2.0
 * @see RemoteAclRuleEventsBridge
 */
@AutoConfiguration
@ConditionalOnBusEnabled
@ConditionalOnProperty(name = "geoserver.bus.enabled", havingValue = "true", matchIfMissing = false)
@AutoConfigureAfter(BusAutoConfiguration.class)
@Import({RabbitAutoConfiguration.class})
@RemoteApplicationEventScan(basePackageClasses = {RemoteRuleEvent.class})
@Slf4j(topic = "org.geoserver.acl.autoconfigure.messaging.bus")
public class AclSpringCloudBusAutoConfiguration {

    private RabbitProperties rabbitProperties;
    private BusProperties busProperties;

    AclSpringCloudBusAutoConfiguration(RabbitProperties rabbitProperties, BusProperties busProperties) {
        this.rabbitProperties = rabbitProperties;
        this.busProperties = busProperties;
    }

    @PostConstruct
    void log() {
        log.info(
                "Spring Cloud Bus integration enabled. rabbit[host: {}:{}, user: {}, virtual-host: {}], bus[id: {}, destination: {}]",
                rabbitProperties.getHost(),
                rabbitProperties.getPort(),
                rabbitProperties.getUsername(),
                rabbitProperties.getVirtualHost(),
                busProperties.getId(),
                busProperties.getDestination());
    }

    @Bean
    RemoteAclRuleEventsBridge remoteAclRuleEventsBridge(
            ApplicationEventPublisher publisher,
            ServiceMatcher serviceMatcher,
            Destination.Factory destinationFactory) {

        return new RemoteAclRuleEventsBridge(publisher, serviceMatcher, destinationFactory);
    }
}
