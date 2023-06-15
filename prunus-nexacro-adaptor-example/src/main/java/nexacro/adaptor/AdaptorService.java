package nexacro.adaptor;

import nexacro.adaptor.dto.Desktop;
import nexacro.adaptor.dto.Equipment;
import nexacro.adaptor.dto.Laptop;
import org.springframework.data.domain.Pageable;
import prunus.persistence.data.pagination.Pagination;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@org.springframework.stereotype.Service
public class AdaptorService {

    public Equipment normal(Equipment equipment, Pageable pageable) {

        List<Laptop> laptops = IntStream.range(0,5)
                .mapToObj(index -> new Laptop(String.valueOf(index), equipment.getLaptops().get(0).getVendor(), equipment.getLaptops().get(0).getDisplaySize()))
                .collect(Collectors.toList());

        return Equipment.builder()
                .id(equipment.getId())
                .name(equipment.getName())
                .desktop(equipment.getDesktop())
                .laptops(laptops)
                .pagination(Pagination.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()))
                .build();
    }

    public Equipment normal(
            String id,
            String name,
            Desktop desktop,
            List<Laptop> laptops,
            Pageable pageable) throws Exception {
        return normal(Equipment.builder()
                .id(id)
                .name(name)
                .desktop(desktop)
                .laptops(laptops)
                .pagination(Pagination.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()))
                .build(), pageable);
    }
}
