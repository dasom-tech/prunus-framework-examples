package persistence.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import prunus.persistence.data.audit.entity.AuditableEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
@AttributeOverrides({
        @AttributeOverride(name="createdBy", column=@Column(name="CREATED_BY")),
        @AttributeOverride(name="createdDate", column=@Column(name="CREATED_DATE")),
        @AttributeOverride(name="modifiedBy", column=@Column(name="MODIFIED_BY")),
        @AttributeOverride(name="modifiedDate", column=@Column(name="MODIFIED_DATE"))
})
public class AuditEntity extends AuditableEntity {

    @PersistDept
    @Column(name="CREATED_DEPT")
    private String createDept;

    @UpdateDept
    @Column(name="MODIFIED_DEPT")
    private String updateDept;
}
