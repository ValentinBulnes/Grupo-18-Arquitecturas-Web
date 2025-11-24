package com.example.chatms.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    // === MONOPATINES ===
    @Value("${spring.data.mongodb.monopatines.uri}")
    private String monopatinesUri;

    @Value("${spring.data.mongodb.monopatines.database}")
    private String monopatinesDb;

    @Bean(name = "mongoClientMonopatines")
    public MongoClient mongoClientMonopatines() {
        return MongoClients.create(monopatinesUri);
    }

    @Bean(name = "mongoTemplateMonopatines")
    public MongoTemplate mongoTemplateMonopatines() {
        return new MongoTemplate(mongoClientMonopatines(), monopatinesDb);
    }

    // === VIAJES ===
    @Value("${spring.data.mongodb.viajes.uri}")
    private String viajesUri;

    @Value("${spring.data.mongodb.viajes.database}")
    private String viajesDb;

    @Bean(name = "mongoClientViajes")
    public MongoClient mongoClientViajes() {
        return MongoClients.create(viajesUri);
    }

    @Bean(name = "mongoTemplateViajes")
    public MongoTemplate mongoTemplateViajes() {
        return new MongoTemplate(mongoClientViajes(), viajesDb);
    }
}
