package persistence.mybatis.controller.h2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import persistence.mybatis.dto.Equipment;
import persistence.mybatis.dto.LaptopDto;
import persistence.mybatis.dto.LaptopReq;
import persistence.mybatis.service.h2.PersistenceMybatisService;

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
    public List<LaptopDto> getPageablePageList(LaptopReq laptopReq, Pageable pageable) {
        return service.getPageablePageList(laptopReq, pageable);
    }

    @GetMapping("/pageable/page")
    public Page<LaptopDto> getPageablePage(LaptopReq laptopReq, Pageable pageable) {
        return service.getPage(laptopReq, pageable);
    }

    @GetMapping("/dto/page")
    public Page<LaptopDto> getPage(LaptopReq laptopReq) {
        return service.getPage(laptopReq, laptopReq.pageable());
    }

    @PostMapping("/post/page")
    public Page<LaptopDto> getPageByPostMethod(@RequestBody Equipment equipment) {
        LaptopReq laptopReq = LaptopReq.builder().vendor(equipment.getVendor()).build();
        Pageable pageable = equipment.getPagination().pageable();
        return service.getPage(laptopReq, pageable);
    }


    @PostMapping
    public void add(@RequestBody LaptopDto laptopDto) {
        service.add(laptopDto);
    }

    @PutMapping
    public void modify(@RequestBody LaptopDto laptopDto) {
        service.modify(laptopDto);
    }

    @PutMapping("/remove")
    public void remove(@RequestBody LaptopDto laptopDto) {
        service.remove(laptopDto.getId());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}