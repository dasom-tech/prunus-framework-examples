package nexacro.adaptor;

import nexacro.adaptor.dto.Desktop;
import nexacro.adaptor.dto.Equipment;
import nexacro.adaptor.dto.Laptop;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import prunus.persistence.data.pagination.Pagination;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class AdaptorService {

    public Equipment getEquipment(Equipment equipment, Pageable pageable) {

        List<Laptop> laptops = IntStream.range(0,5)
                .mapToObj(index -> new Laptop(String.valueOf(index), equipment.getLaptops().get(0).getVendor(), equipment.getLaptops().get(0).getDisplaySize()))
                .collect(Collectors.toList());

        return Equipment.builder()
                .id(equipment.getId())
                .seq(equipment.getSeq())
                .name(equipment.getName())
                .desktop(equipment.getDesktop())
                .laptops(laptops)
                .pagination(Pagination.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()))
                .build();
    }

    public Equipment getEquipment(
            String id,
            String name,
            int seq,
            Desktop desktop,
            List<Laptop> laptops,
            Pageable pageable) {
        return getEquipment(Equipment.builder()
                .id(id)
                .name(name)
                .seq(seq)
                .desktop(desktop)
                .laptops(laptops)
                .pagination(Pagination.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()))
                .build(), pageable);
    }
}
