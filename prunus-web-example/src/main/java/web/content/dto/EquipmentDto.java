package web.content.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EquipmentDto extends CachedDto {

    private LaptopDto laptop;

    private DesktopDto desktop;
}
