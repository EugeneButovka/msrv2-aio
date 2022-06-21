package com.msrv2.inventoryservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.msrv2.inventoryservice.repository")
public class MongoConfig {

}

