package com.partial.update.configurations;


import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.config.ProgrammaticDriverConfigLoaderBuilder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.config.SessionBuilderConfigurer;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.time.Duration;

@Getter
@Setter
@EnableCassandraRepositories
@ConfigurationProperties(prefix = "spring.data.cassandra")
@Configuration("cassandraProperties")
public class CassandraConfig extends AbstractCassandraConfiguration {
    private String keyspaceName;
    private String contactPoints;
    private int port;
    private String localDataCenter;
    private int timeout;

    @Bean
    @Override
    public CqlSessionFactoryBean cassandraSession() {
        CqlSessionFactoryBean cassandraSession = super.cassandraSession();
        cassandraSession.setSessionBuilderConfigurer(getSessionBuilderConfigurer());
        cassandraSession.setKeyspaceName(keyspaceName);
        cassandraSession.setLocalDatacenter(localDataCenter);
        cassandraSession.setContactPoints(contactPoints);
        cassandraSession.setPort(port);

        return cassandraSession;
    }

    @Override
    protected SessionBuilderConfigurer getSessionBuilderConfigurer() {
        return cqlSessionBuilder -> {
            ProgrammaticDriverConfigLoaderBuilder config = DriverConfigLoader.programmaticBuilder()
                    .withDuration(DefaultDriverOption.CONNECTION_INIT_QUERY_TIMEOUT, Duration.ofSeconds(timeout))
                    .withBoolean(DefaultDriverOption.RECONNECT_ON_INIT, true)
                    .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(timeout))
                    .withDuration(DefaultDriverOption.CONTROL_CONNECTION_TIMEOUT, Duration.ofSeconds(timeout));

            return cqlSessionBuilder.withConfigLoader(config.build());
        };
    }
}
