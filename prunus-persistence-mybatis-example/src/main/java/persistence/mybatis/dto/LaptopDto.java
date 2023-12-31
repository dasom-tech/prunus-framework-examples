package persistence.mybatis.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import persistence.mybatis.entity.Laptop;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LaptopDto {
    private long id;

    private String vendor;

    private int displaySize;

    @JsonIgnore
    private boolean deleted;

    public static LaptopDto of(Laptop laptop) {
        return LaptopDto.builder()
                .id(laptop.getId())
                .vendor(laptop.getVendor())
                .displaySize(laptop.getDisplaySize())
                .deleted(laptop.getDeleted())
                .build();
    }
}
