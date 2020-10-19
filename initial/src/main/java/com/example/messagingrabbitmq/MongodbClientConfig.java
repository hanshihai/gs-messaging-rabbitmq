package com.example.messagingrabbitmq;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongodbClientConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.mongodb.database}") private String dbname;
    @Value("${spring.mongodb.connection}") private String dbconnection;

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(dbconnection);
    }

    @Override
    protected String getDatabaseName() {
        return dbname;
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), dbname);
    }
}
