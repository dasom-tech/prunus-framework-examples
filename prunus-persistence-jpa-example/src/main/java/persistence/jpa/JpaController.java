package persistence.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import persistence.jpa.dto.LaptopDto;

import java.util.List;

@RestController
public class JpaController {

    private final JpaService service;

    public JpaController(JpaService service) {
        this.service = service;
    }

    @GetMapping
    public List<LaptopDto> getAll(LaptopDto laptopDto) {
        return service.getAll(laptopDto);
    }

    @GetMapping("/pagelist")
    public List<LaptopDto> getPageList(LaptopDto laptopDto, Pageable pageable) {
        return service.getPageList(laptopDto, pageable);
    }

    @GetMapping("/page")
    public Page<LaptopDto> getPage(LaptopDto laptopDto, Pageable pageable) {
        return service.getPage(laptopDto, pageable);
    }
}
