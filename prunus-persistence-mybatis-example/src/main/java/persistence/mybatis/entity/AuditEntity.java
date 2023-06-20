package persistence.mybatis.entity;

import lombok.Getter;
import lombok.Setter;
import prunus.persistence.data.audit.entity.AuditableEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;

@Getter
@Setter
@AttributeOverrides({
        @AttributeOverride(name="createdBy", column=@Column(name="CRE_USER")),
        @AttributeOverride(name="createdDate", column=@Column(name="CRE_DATE")),
        @AttributeOverride(name="modifiedBy", column=@Column(name="MOD_USER")),
        @AttributeOverride(name="modifiedDate", column=@Column(name="MOD_DATE"))
})
public class AuditEntity extends AuditableEntity {

    @PersistDept
    @Column(name="CRE_DEPT")
    private String createDept;

    @UpdateDept
    @Column(name="MOD_DEPT")
    private String updateDept;
}
