package persistence.jpa.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="LAPTOP")
@SequenceGenerator(name = "ID_GENERATOR", sequenceName = "LAPTOP_ID_SEQUENCE", initialValue = 1, allocationSize = 1)
public class Laptop extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_GENERATOR")
    private long id;

    @Column(length = 100, nullable = false)
    private String vendor;

    @Column(nullable = false)
    private int displaySize;

    @Column(nullable = false)
    @Setter
    private Boolean deleted;

    public void updateSpec(String vendor, int displaySize) {
        this.vendor = vendor;
        this.displaySize = displaySize;
    }

    public void updateDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
