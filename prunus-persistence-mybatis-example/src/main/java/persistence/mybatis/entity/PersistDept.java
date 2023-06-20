package persistence.mybatis.entity;

import prunus.persistence.data.audit.entity.AuditType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static prunus.persistence.data.audit.entity.AuditType.PERSIST;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = FIELD)
public @interface PersistDept {

    AuditType type() default PERSIST;
    String providerName() default "auditingDeptProvider";
}
