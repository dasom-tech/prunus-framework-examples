package persistence.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import persistence.jpa.dto.Equipment;
import persistence.jpa.dto.LaptopDto;
import persistence.jpa.dto.LaptopReq;

import java.util.List;

@RestController
public class JpaController {

    private final JpaService service;

    public JpaController(JpaService service) {
        this.service = service;
    }

    @GetMapping
    public List<LaptopDto> getAll(LaptopReq laptopReq) {
        return service.getAll(laptopReq);
    }

    @GetMapping("/pageable/pagelist")
    public List<LaptopDto> getPageList(LaptopReq laptopReq, Pageable pageable) {
        return service.getPageList(laptopReq, pageable);
    }

    @GetMapping("/pageable/page")
    public Page<LaptopDto> getPage(LaptopReq laptopReq, Pageable pageable) {
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
