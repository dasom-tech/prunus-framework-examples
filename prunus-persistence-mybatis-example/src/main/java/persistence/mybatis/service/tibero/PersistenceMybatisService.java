package persistence.mybatis.service.tibero;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import persistence.mybatis.datasource.DataSourceNameContextHolder;
import persistence.mybatis.dto.LaptopDto;
import persistence.mybatis.entity.Laptop;
import persistence.mybatis.mapper.tibero.PersistenceMybatisMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service("tibero.persistenceMybatisService")
public class PersistenceMybatisService {
    private final PersistenceMybatisMapper mapper;

    public PersistenceMybatisService(PersistenceMybatisMapper mapper) {
        this.mapper = mapper;
    }

    public List<LaptopDto> getAll(LaptopDto laptopDto) {
        DataSourceNameContextHolder.set("tibero");
        List<Laptop> laptops = mapper.selectAll(laptopDto);
        return laptops.stream().map(LaptopDto::of).collect(Collectors.toList());
    }

    public List<LaptopDto> getPageList(LaptopDto laptopDto, Pageable pageable) {
        DataSourceNameContextHolder.set("tibero");
        List<Laptop> laptops = mapper.selectPageList(laptopDto, pageable);
        return laptops.stream().map(LaptopDto::of).collect(Collectors.toList());
    }

    public Page<LaptopDto> getPage(LaptopDto laptopDto, Pageable pageable) {
        DataSourceNameContextHolder.set("tibero");
        Page<Laptop> laptops = mapper.selectPage(laptopDto, pageable);
        return new PageImpl<>(laptops.stream().map(LaptopDto::of).collect(Collectors.toList()), pageable, laptops.getTotalElements());
    }

    public void add(LaptopDto laptopDto) {
        DataSourceNameContextHolder.set("tibero");
        Laptop laptop = Laptop.builder()
                .vendor(laptopDto.getVendor())
                .displaySize(laptopDto.getDisplaySize())
                .build();
        mapper.insert(laptop);
    }

    public void modify(LaptopDto laptopDto) {
        DataSourceNameContextHolder.set("tibero");
        Laptop laptop = Laptop.builder()
                .id(laptopDto.getId())
                .vendor(laptopDto.getVendor())
                .displaySize(laptopDto.getDisplaySize())
                .build();
        mapper.update(laptop);
    }

    public void remove(String id) {
        DataSourceNameContextHolder.set("tibero");
        mapper.delete(id);
    }

    public void merge(LaptopDto laptopDto) {
        DataSourceNameContextHolder.set("tibero");
        Laptop laptop = Laptop.builder()
                .id(laptopDto.getId())
                .vendor(laptopDto.getVendor())
                .displaySize(laptopDto.getDisplaySize())
                .build();
        mapper.merge(laptop);
    }
}
