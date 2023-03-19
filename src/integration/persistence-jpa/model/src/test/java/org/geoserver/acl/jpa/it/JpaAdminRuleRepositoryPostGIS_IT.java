package org.geoserver.acl.jpa.it;

import org.geoserver.acl.jpa.config.AuthorizationDataSourceConfiguration;
import org.geoserver.acl.jpa.config.AuthorizationJPAConfiguration;
import org.geoserver.acl.jpa.config.AuthorizationJPAPropertiesTestConfiguration;
import org.geoserver.acl.jpa.repository.JpaAdminRuleRepositoryTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.transaction.Transactional;

@Testcontainers(disabledWithoutDocker = true)
@Transactional
@SpringBootTest(
        classes = {
            AuthorizationJPAPropertiesTestConfiguration.class,
            AuthorizationDataSourceConfiguration.class,
            AuthorizationJPAConfiguration.class
        })
// see config props in src/test/resource/application-test.yaml
@ActiveProfiles("test")
class JpaAdminRuleRepositoryPostGIS_IT extends JpaAdminRuleRepositoryTest {

    private static final DockerImageName POSTGIS_IMAGE_NAME =
            DockerImageName.parse("postgis/postgis").asCompatibleSubstituteFor("postgres");

    @Container
    static PostgreSQLContainer<?> postgis = new PostgreSQLContainer<>(POSTGIS_IMAGE_NAME);

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("geoserver.acl.datasource.url", () -> postgis.getJdbcUrl());
        registry.add("geoserver.acl.datasource.username", postgis::getUsername);
        registry.add("geoserver.acl.datasource.password", postgis::getPassword);
    }
}