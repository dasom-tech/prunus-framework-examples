package web.content;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.content.dto.DesktopDto;
import web.content.dto.EquipmentDto;
import web.content.dto.LaptopDto;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/content")
public class ConteCachingController {

    @PostMapping("/laptop/add")
    public LaptopDto addLaptop(@RequestBody LaptopDto laptopDto) {
        return new LaptopDto(2, "lg", laptopDto.getDisplaySize(), false);
    }

    @PostMapping("/desktop/modify")
    public DesktopDto addDesktop(@RequestBody DesktopDto desktopDto) {
        return new DesktopDto(3, desktopDto.getVendor());
    }

    @PostMapping("/equipment/add")
    public EquipmentDto addEquipment(@RequestBody EquipmentDto equipmentDto, HttpServletRequest request) throws IOException {
        log.debug("controller addEquipment read request body :\n{}", IOUtils.toString(request.getInputStream(), request.getCharacterEncoding()));
        return new EquipmentDto(
                new LaptopDto(2, "lg", equipmentDto.getLaptop().getDisplaySize(), false),
                new DesktopDto(3, equipmentDto.getDesktop().getVendor())
        );
    }
}
