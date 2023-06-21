package nexacro.adaptor;

import nexacro.adaptor.dto.Desktop;
import nexacro.adaptor.dto.Equipment;
import nexacro.adaptor.dto.Laptop;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import prunus.nexacro.adaptor.resolver.annotation.DataSet;
import prunus.nexacro.adaptor.resolver.annotation.Variable;
import prunus.persistence.data.pagination.Pagination;

import java.util.List;

/**
 * nexacro clinet request http method 정책
 * - <a href="https://www.playnexacro.com/#show:learn:4578">...</a>
 */
@RestController
public class AdaptorController {

    private final AdaptorService service;

    public AdaptorController(AdaptorService service) {
        this.service = service;
    }

    @PostMapping("/resolver")
    public Equipment resolverNormal(
            @Variable(name="id", required = true) String id,
            @Variable("name") String name,
            @Variable("seq") int seq,
            @DataSet(name="ds_desktop", required = true) Desktop desktop,
            @DataSet("laptops") List<Laptop> laptops,
            @DataSet("ds_paging") Pagination pagination) {
        Pageable pageable = pagination.pageable();
        return service.getEquipment(id, name, seq, desktop, laptops, pageable);
    }

    @PostMapping("/converter")
    public Equipment converterNormal(@RequestBody Equipment equipment) {
        Pageable pageable = equipment.getPagination().pageable();
        return service.getEquipment(equipment, pageable);
    }
}
