package com.test.record.data;

import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RecordDataConfig {
    @Value("${com.test.record.data.jdbc.driver}")
	protected String driverNameDb;
	
	@Value("${com.test.record.data.jdbc.url}")
	protected String urlDb;

    @Value("${com.test.record.data.jdbc.username}")
    protected String userNameDb;

    @Value("${com.test.record.data.jdbc.password}")
    protected String passwordDb;
}
