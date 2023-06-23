package web.content.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class LaptopDto {

    private long id;

    private String vendor;

    private int displaySize;

    @JsonIgnore
    private boolean deleted;
}
