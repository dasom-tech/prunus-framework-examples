package persistence.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping
    public LaptopDto add(@RequestBody LaptopDto laptopDto) throws Exception {
        return service.add(laptopDto);
    }

    @PutMapping
    public LaptopDto modify(@RequestBody LaptopDto laptopDto) throws Exception {
        return service.modify(laptopDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable long id) throws Exception {
        service.remove(id);
    }
}
