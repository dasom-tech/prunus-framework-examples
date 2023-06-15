package persistence.jpa.entity;

import lombok.*;
import prunus.persistence.data.audit.entity.AuditableEntity;

import javax.persistence.*;

@Entity(name="LAPTOP")
@SequenceGenerator(name = "ID_GENERATOR", sequenceName = "LAPTOP_ID_SEQUENCE", initialValue = 1, allocationSize = 1)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Laptop extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_GENERATOR")
    private long id;

    @Column(length = 100, nullable = false)
    private String vendor;

    @Column(nullable = false)
    private int displaySize;

    @Column(nullable = false)
    @Setter
    private boolean deleted;
}
