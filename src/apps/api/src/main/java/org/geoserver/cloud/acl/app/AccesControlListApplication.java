package org.geoserver.cloud.acl.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccesControlListApplication {

    public static void main(String... args) {
        try {
            SpringApplication.run(AccesControlListApplication.class, args);
        } catch (RuntimeException e) {
            System.exit(-1);
        }
    }
}