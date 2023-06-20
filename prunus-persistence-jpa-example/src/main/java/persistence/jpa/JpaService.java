package persistence.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import persistence.jpa.dto.LaptopDto;
import persistence.jpa.entity.Laptop;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JpaService {

    private final JpaRepository repository;

    public JpaService(JpaRepository repository) {
        this.repository = repository;
    }

    public List<LaptopDto> getAll(LaptopDto laptopDto) {
        List<Laptop> laptops = repository.findAllByVendorAndDeletedIsFalse(laptopDto.getVendor());
        return laptops.stream().map(LaptopDto::of).collect(Collectors.toList());
    }

    public List<LaptopDto> getPageList(LaptopDto laptopDto, Pageable pageable) {
        Page<Laptop> laptops = repository.findAllByVendorAndDeletedIsFalse(laptopDto.getVendor(), pageable);
        return laptops.stream().map(LaptopDto::of).collect(Collectors.toList());
    }

    public Page<LaptopDto> getPage(LaptopDto laptopDto, Pageable pageable) {
        Page<Laptop> laptops = repository.findAllByVendorAndDeletedIsFalse(laptopDto.getVendor(), pageable);
        return new PageImpl<>(laptops.stream().map(LaptopDto::of).collect(Collectors.toList()), pageable, laptops.getTotalElements());
    }

    public LaptopDto add(LaptopDto laptopDto) throws Exception {
        if(repository.existsById(laptopDto.getId())) throw new Exception("already exists");
        Laptop laptop = Laptop.builder()
                .vendor(laptopDto.getVendor())
                .displaySize(laptopDto.getDisplaySize())
                .deleted(Boolean.FALSE)
                .build();
        return LaptopDto.of(repository.save(laptop));
    }

    public LaptopDto modify(LaptopDto laptopDto) throws Exception {
        Laptop laptop = repository.findById(laptopDto.getId()).orElseThrow(() -> new Exception("not found"));
        laptop.updateSpec(laptopDto.getVendor(), laptopDto.getDisplaySize());
        return LaptopDto.of(repository.save(laptop));
    }

    public void remove(long id) throws Exception {
        if(!repository.existsById(id)) throw new Exception("not found");
        repository.deleteById(id);
    }
}
