package persistence.jpa.dto;

import lombok.*;
import persistence.jpa.entity.Laptop;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LaptopDto {
    private long id;

    private String vendor;

    private int displaySize;

    private Boolean deleted;

    public static LaptopDto of(Laptop laptop) {
        return LaptopDto.builder()
                .id(laptop.getId())
                .vendor(laptop.getVendor())
                .displaySize(laptop.getDisplaySize())
                .deleted(laptop.getDeleted())
                .build();
    }

    public Laptop toEntity() {
        return Laptop.builder()
                .id(getId())
                .vendor(getVendor())
                .displaySize(getDisplaySize())
                .deleted(getDeleted())
                .build();
    }
}
