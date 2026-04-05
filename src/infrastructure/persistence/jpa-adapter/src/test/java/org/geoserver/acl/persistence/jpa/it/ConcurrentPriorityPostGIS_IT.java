/* (c) 2026  Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.acl.persistence.jpa.it;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Tests concurrent priority operations against PostgreSQL using {@code pg_advisory_xact_lock}.
 *
 * @see <a href="https://github.com/geoserver/geoserver-acl/issues/84">#84</a>
 */
@Testcontainers(disabledWithoutDocker = true)
class ConcurrentPriorityPostGIS_IT extends ConcurrentPriorityBaseTest {

    private static final DockerImageName POSTGIS_IMAGE_NAME =
            DockerImageName.parse("imresamu/postgis:15-3.4").asCompatibleSubstituteFor("postgres");

    @Container
    static PostgreSQLContainer postgis = new PostgreSQLContainer(POSTGIS_IMAGE_NAME);

    @DynamicPropertySource
    static void registerPostgresProperties(DynamicPropertyRegistry registry) {
        registry.add("geoserver.acl.datasource.url", postgis::getJdbcUrl);
        registry.add("geoserver.acl.datasource.username", postgis::getUsername);
        registry.add("geoserver.acl.datasource.password", postgis::getPassword);
    }
}
