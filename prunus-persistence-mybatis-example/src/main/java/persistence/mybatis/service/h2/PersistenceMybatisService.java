package persistence.mybatis.service.h2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import persistence.mybatis.dto.LaptopDto;
import persistence.mybatis.dto.LaptopReq;
import persistence.mybatis.entity.Laptop;
import persistence.mybatis.mapper.h2.PersistenceMybatisMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service("h2.persistenceMybatisService")
public class PersistenceMybatisService {
    private final PersistenceMybatisMapper mapper;

    public PersistenceMybatisService(PersistenceMybatisMapper mapper) {
        this.mapper = mapper;
    }

    public List<LaptopDto> getAll(LaptopReq laptopReq) {
        List<Laptop> laptops = mapper.selectAll(laptopReq);
        return laptops.stream().map(LaptopDto::of).collect(Collectors.toList());
    }

    public List<LaptopDto> getPageablePageList(LaptopReq laptopReq, Pageable pageable) {
        List<Laptop> laptops = mapper.selectPageList(laptopReq, pageable);
        return laptops.stream().map(LaptopDto::of).collect(Collectors.toList());
    }

    public Page<LaptopDto> getPage(LaptopReq laptopReq, Pageable pageable) {
        Page<Laptop> laptops = mapper.selectPage(laptopReq, pageable);
        return new PageImpl<>(laptops.stream().map(LaptopDto::of).collect(Collectors.toList()), pageable, laptops.getTotalElements());
    }

    public void add(LaptopDto laptopDto) {
        Laptop laptop = Laptop.builder()
                .vendor(laptopDto.getVendor())
                .displaySize(laptopDto.getDisplaySize())
                .build();
        mapper.insert(laptop);
    }

    public void modify(LaptopDto laptopDto) {
        Laptop laptop = Laptop.builder()
                .id(laptopDto.getId())
                .vendor(laptopDto.getVendor())
                .displaySize(laptopDto.getDisplaySize())
                .build();
        mapper.update(laptop);
    }

    public void remove(Long id) {
        Laptop laptop = Laptop.builder()
                .id(id)
                .build();
        mapper.remove(laptop);
    }

    public void delete(Long id) {
        Laptop laptop = Laptop.builder()
                .id(id)
                .build();
        mapper.delete(laptop);
    }
}
