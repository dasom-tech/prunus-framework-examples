package persistence.jpa.provider;

import prunus.persistence.data.audit.provider.AuditingAware;

public class AuditingUserSubjectProvider implements AuditingAware<String> {
    @Override
    public String provide() {
        return "exampleUser";
    }
}