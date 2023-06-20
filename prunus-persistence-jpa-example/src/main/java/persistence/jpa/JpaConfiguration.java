package persistence.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import persistence.jpa.provider.AuditingUserDeptProvider;
import persistence.jpa.provider.AuditingUserSubjectProvider;
import prunus.persistence.data.audit.provider.AuditingAware;

import static prunus.persistence.data.audit.provider.AuditProviderSupport.SUBJECT_PROVIDER_BEAN_NAME;

@Configuration
@EnableJpaRepositories(basePackages = { "persistence.jpa" })
public class JpaConfiguration {
    @Bean(SUBJECT_PROVIDER_BEAN_NAME)
    public AuditingAware<String> auditingUserSubjectProvider() {
        return new AuditingUserSubjectProvider();
    }

    @Bean("auditingDeptProvider")
    public AuditingAware<String> auditingUserDeptProvider() {
        return new AuditingUserDeptProvider();
    }

}
