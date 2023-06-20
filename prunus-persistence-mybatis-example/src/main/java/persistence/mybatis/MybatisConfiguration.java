package persistence.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;
import persistence.mybatis.datasource.DataSourceConfig;
import persistence.mybatis.datasource.HikariDataSourceConfig;
import persistence.mybatis.provider.AuditingUserDeptProvider;
import persistence.mybatis.provider.AuditingUserSubjectProvider;
import prunus.persistence.data.audit.provider.AuditingAware;

import static prunus.persistence.data.audit.provider.AuditProviderSupport.SUBJECT_PROVIDER_BEAN_NAME;

@Import({HikariDataSourceConfig.class, DataSourceConfig.class})
@MapperScan(basePackages = "persistence.mybatis", annotationClass = Repository.class)
@Configuration
public class MybatisConfiguration {

    @Bean(SUBJECT_PROVIDER_BEAN_NAME)
    public AuditingAware<String> auditingUserSubjectProvider() {
        return new AuditingUserSubjectProvider();
    }

    @Bean("auditingDeptProvider")
    public AuditingAware<String> auditingUserDeptProvider() {
        return new AuditingUserDeptProvider();
    }
}
