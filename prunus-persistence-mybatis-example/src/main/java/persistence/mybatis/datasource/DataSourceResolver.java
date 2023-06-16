package persistence.mybatis.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DataSourceResolver {
    private final String defaultDatasourceName;
    private final Map<String, DataSource> dataSources;

    public DataSourceResolver(Map<String, HikariConfig> hikariDataSourceConfig, String defaultDatasourceName) {
        this.dataSources = new HashMap<>(hikariDataSourceConfig.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> new HikariDataSource(entry.getValue()))));
        this.defaultDatasourceName = defaultDatasourceName;
        DataSourceNameContextHolder.set(defaultDatasourceName);
    }

    public Map<String, DataSource> getDataSources() {
        return dataSources;
    }

    public String getDefaultDataSourceName() {
        return this.defaultDatasourceName;
    }
}
