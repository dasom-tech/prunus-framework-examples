package persistence.mybatis.entity;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Laptop extends AuditEntity {

    private long id;

    private String vendor;

    private int displaySize;

    private Boolean deleted;
}
