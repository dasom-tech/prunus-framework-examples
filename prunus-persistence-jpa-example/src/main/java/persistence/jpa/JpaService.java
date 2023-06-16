package persistence.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import persistence.jpa.dto.LaptopDto;
import persistence.jpa.entity.Laptop;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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
}
