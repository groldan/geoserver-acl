/* (c) 2026  Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.acl.persistence.jpa.it;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

/**
 * Tests concurrent priority operations against H2 using
 * {@code pg_advisory_xact_lock}.
 *
 * @see <a href="https://github.com/geoserver/geoserver-acl/issues/84">#84</a>
 */
class ConcurrentPriorityTest extends ConcurrentPriorityBaseTest {

    @DynamicPropertySource
    static void registerPostgresProperties(DynamicPropertyRegistry registry) {
        registry.add("geoserver.acl.datasource.url", () -> "jdbc:h2:mem:geoserver-acl-test");
    }
}
