package com.example.chatms.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Configuración de múltiples DataSources para acceder a todas las bases de datos
 * de los diferentes microservicios.
 */
@Configuration
public class MultiDataSourceConfig {

    // ====================================================================
    // DataSource: PARADAS (Principal/Default)
    // ====================================================================
    @Primary
    @Bean(name = "paradasDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.paradas")
    public DataSource paradasDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "paradasJdbcTemplate")
    public JdbcTemplate paradasJdbcTemplate() {
        return new JdbcTemplate(paradasDataSource());
    }

    // ====================================================================
    // DataSource: USUARIOS
    // ====================================================================
    @Bean(name = "usuariosDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.usuarios")
    public DataSource usuariosDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "usuariosJdbcTemplate")
    public JdbcTemplate usuariosJdbcTemplate() {
        return new JdbcTemplate(usuariosDataSource());
    }

    // ====================================================================
    // DataSource: VIAJES
    // ====================================================================
    @Bean(name = "viajesDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.viajes")
    public DataSource viajesDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "viajesJdbcTemplate")
    public JdbcTemplate viajesJdbcTemplate() {
        return new JdbcTemplate(viajesDataSource());
    }

    // ====================================================================
    // DataSource: FACTURAS
    // ====================================================================
    @Bean(name = "facturasDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.facturas")
    public DataSource facturasDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "facturasJdbcTemplate")
    public JdbcTemplate facturasJdbcTemplate() {
        return new JdbcTemplate(facturasDataSource());
    }

    // ====================================================================
    // DataSource: TARIFAS
    // ====================================================================
    @Bean(name = "tarifasDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.tarifas")
    public DataSource tarifasDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "tarifasJdbcTemplate")
    public JdbcTemplate tarifasJdbcTemplate() {
        return new JdbcTemplate(tarifasDataSource());
    }
}
