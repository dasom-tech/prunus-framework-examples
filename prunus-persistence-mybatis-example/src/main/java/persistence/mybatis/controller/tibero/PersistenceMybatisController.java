package persistence.mybatis.controller.tibero;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import persistence.mybatis.dto.LaptopDto;
import persistence.mybatis.service.tibero.PersistenceMybatisService;

import java.util.List;

@RequestMapping("/tibero")
@RestController("tibero.persistenceMybatisController")
public class PersistenceMybatisController {

    private final PersistenceMybatisService service;

    public PersistenceMybatisController(PersistenceMybatisService service) {
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
    public void add(@RequestBody LaptopDto laptopDto) {
        service.add(laptopDto);
    }

    @PutMapping
    public void modify(@RequestBody LaptopDto laptopDto) {
        service.modify(laptopDto);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable String id) {
        service.remove(id);
    }
}