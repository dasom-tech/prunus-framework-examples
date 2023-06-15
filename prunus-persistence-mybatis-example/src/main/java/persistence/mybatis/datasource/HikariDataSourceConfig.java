package persistence.mybatis.datasource;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

public class HikariDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix="spring.datasource.hikari.h2")
    public HikariConfig h2() {
        return new HikariConfig();
    }

    @Bean
    @ConfigurationProperties(prefix="spring.datasource.hikari.tibero")
    public HikariConfig tibero() {
        return new HikariConfig();
    }
}
