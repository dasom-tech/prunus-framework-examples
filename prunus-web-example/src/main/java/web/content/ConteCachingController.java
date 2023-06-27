package web.content;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import prunus.core.util.ServletContextHolder;
import prunus.web.content.ServletContentCachingFilter;
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
        LaptopDto laptop = new LaptopDto(2, "lg", laptopDto.getDisplaySize(), false);
        Integer cachedBodySize = getCachedBodySize(ServletContentCachingFilter.CACHED_REQUEST_BODY_SIZE);
        if(cachedBodySize != null) laptop.setRequestBodySize(cachedBodySize);
        return laptop;
    }

    @PostMapping("/desktop/modify")
    public DesktopDto addDesktop(@RequestBody DesktopDto desktopDto) {
        DesktopDto desktop = new DesktopDto(3, desktopDto.getVendor());
        Integer cachedBodySize = getCachedBodySize(ServletContentCachingFilter.CACHED_REQUEST_BODY_SIZE);
        if(cachedBodySize != null) desktop.setRequestBodySize(cachedBodySize);
        return desktop;
    }

    @PostMapping("/equipment/add")
    public EquipmentDto addEquipment(@RequestBody EquipmentDto equipmentDto, HttpServletRequest request) throws IOException {
        log.debug("controller addEquipment read request body :\n{}", IOUtils.toString(request.getInputStream(), request.getCharacterEncoding()));
        EquipmentDto equipment = new EquipmentDto(
                new LaptopDto(2, "lg", equipmentDto.getLaptop().getDisplaySize(), false),
                new DesktopDto(3, equipmentDto.getDesktop().getVendor())
        );
        Integer cachedBodySize = getCachedBodySize(ServletContentCachingFilter.CACHED_REQUEST_BODY_SIZE);
        if(cachedBodySize != null) equipment.setRequestBodySize(cachedBodySize);
        return equipment;
    }

    private Integer getCachedBodySize(String name) {
        Object cachedBodySize = ServletContextHolder.servletRequestAttributes().getAttribute(name, RequestAttributes.SCOPE_REQUEST);
        return cachedBodySize != null ? Integer.parseInt(String.valueOf(cachedBodySize)) : null;
    }
}
