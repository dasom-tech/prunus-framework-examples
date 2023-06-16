package persistence.mybatis.datasource;

import org.springframework.util.Assert;

public class DataSourceNameContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    private DataSourceNameContextHolder() {}

    public static void set(String dataSourceName) {
        Assert.hasText(dataSourceName, "DataSource name must has text");
        contextHolder.set(dataSourceName);
    }

    public static String get() {
        return contextHolder.get();
    }
}