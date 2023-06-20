package persistence.mybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Laptop extends AuditEntity {

    private long id;

    private String vendor;

    private int displaySize;

    private Boolean deleted;
}
