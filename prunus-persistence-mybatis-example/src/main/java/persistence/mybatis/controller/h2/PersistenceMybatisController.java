package persistence.mybatis.controller.h2;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import persistence.mybatis.dto.LaptopDto;
import persistence.mybatis.dto.LaptopReq;
import persistence.mybatis.service.h2.PersistenceMybatisService;
import prunus.persistence.data.pagination.Pagination;

import java.util.List;

@RequestMapping("/h2")
@RestController("h2.persistenceMybatisController")
public class PersistenceMybatisController {

    private final PersistenceMybatisService service;

    public PersistenceMybatisController(PersistenceMybatisService service) {
        this.service = service;
    }

    @GetMapping
    public List<LaptopDto> getAll(LaptopReq laptopReq) {
        return service.getAll(laptopReq);
    }

    @GetMapping("/pageable/pagelist")
    public List<LaptopDto> getPageablePageList(LaptopReq laptopReq, Pagination pageable) {
        return service.getPageablePageList(laptopReq, pageable);
    }

    @GetMapping("/pageable/page")
    public Page<LaptopDto> getPageablePage(LaptopReq laptopReq, Pagination pageable) {
        return service.getPage(laptopReq, pageable);
    }

    @GetMapping("/dto/page")
    public Page<LaptopDto> getPage(LaptopReq laptopReq) {
        return service.getPage(laptopReq, laptopReq.pageable());
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

    @PostMapping("/merge")
    public void merge(@RequestBody LaptopDto laptopDto) {
        service.merge(laptopDto);
    }
}