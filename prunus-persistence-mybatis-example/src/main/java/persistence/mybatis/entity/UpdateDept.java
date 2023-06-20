package persistence.mybatis.entity;

import prunus.persistence.data.audit.entity.AuditType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static prunus.persistence.data.audit.entity.AuditType.UPDATE;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = FIELD)
public @interface UpdateDept {

    AuditType type() default UPDATE;
    String providerName() default "auditingDeptProvider";
}
