package com.msrv2.catalogservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.msrv2.catalogservice.repository")
public class MongoConfig {

}

