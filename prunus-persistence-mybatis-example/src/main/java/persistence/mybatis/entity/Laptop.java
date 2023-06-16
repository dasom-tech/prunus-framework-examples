package persistence.mybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import prunus.persistence.data.audit.entity.AuditableEntity;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Laptop extends AuditableEntity {

    private long id;

    private String vendor;

    private int displaySize;

    private boolean deleted;
}
