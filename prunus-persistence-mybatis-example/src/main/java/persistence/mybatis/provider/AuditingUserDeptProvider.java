package persistence.mybatis.provider;

import prunus.persistence.data.audit.provider.AuditingAware;

public class AuditingUserDeptProvider implements AuditingAware<String> {

    @Override
    public String provide() {
        return "exampleDept";
    }
}