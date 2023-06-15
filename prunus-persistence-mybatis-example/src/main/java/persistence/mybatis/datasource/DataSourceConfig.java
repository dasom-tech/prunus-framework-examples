package persistence.mybatis.datasource;

import com.zaxxer.hikari.HikariConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.Map;

@Slf4j
public class DataSourceConfig {

    public static final String DEFAULT_DATASOURCE_NAME = "h2";
    private final Map<String, HikariConfig> hikariDataSourceConfig;

    public DataSourceConfig(Map<String, HikariConfig> hikariDataSourceConfig) {
        this.hikariDataSourceConfig = hikariDataSourceConfig;
    }

    @Bean
    public DataSourceResolver dataSourceResolver() {
        return new DataSourceResolver(hikariDataSourceConfig, DEFAULT_DATASOURCE_NAME);
    }

    @Bean
    public DataSource routingDataSource(DataSourceResolver dataSourceResolver) {
        return new RoutingDataSource(dataSourceResolver);
    }
}
