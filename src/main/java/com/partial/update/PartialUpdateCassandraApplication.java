package com.partial.update;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class PartialUpdateCassandraApplication {

    public static void main(String[] args) {
        SpringApplication.run(PartialUpdateCassandraApplication.class, args);
    }
}
