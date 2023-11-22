package com.test.record.data;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

// @Configuration
// @Profile({"dev","qa","prod"})
// @PropertySource(value = "classpath:test-record-data-${spring.profiles.active}.properties")
public class EnvironmentDataConfig extends RecordDataConfig{
   
    @Autowired
    private Environment environment;
    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(driverNameDb);
        dataSourceBuilder.url(environment.getProperty("TEST_DB_URL"));
        return dataSourceBuilder.build();
    }
}
