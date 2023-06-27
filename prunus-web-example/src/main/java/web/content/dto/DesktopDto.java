package web.content.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DesktopDto extends CachedDto {

    private long id;

    private String vendor;
}
