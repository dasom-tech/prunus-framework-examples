package nexacro.adaptor;

import nexacro.adaptor.dto.Desktop;
import nexacro.adaptor.dto.Equipment;
import nexacro.adaptor.dto.Laptop;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import prunus.nexacro.adaptor.resolver.annotation.DataSetParam;
import prunus.nexacro.adaptor.resolver.annotation.VariableParam;
import prunus.nexacro.adaptor.resolver.support.NexacroResult;
import prunus.persistence.data.pagination.Pagination;

import java.util.List;

/**
 * nexacro clinet request http method 정책
 * - <a href="https://www.playnexacro.com/#show:learn:4578">...</a>
 */
@Controller
public class AdaptorController {

    private final AdaptorService service;

    public AdaptorController(AdaptorService service) {
        this.service = service;
    }

    @PostMapping("/resolver")
    public NexacroResult resolverNormal(
            @VariableParam(name="id", required = true) String id,
            @VariableParam("name") String name,
            @VariableParam("seq") int seq,
            @DataSetParam(name="ds_desktop", required = true) Desktop desktop,
            @DataSetParam("laptops") List<Laptop> laptops,
            @DataSetParam("ds_paging") Pagination pagination) {
        Pageable pageable = pagination.pageable();
        Equipment equipment = service.getEquipment(id, name, seq, desktop, laptops, pageable);
        return NexacroResult.builder()
                .variable("id", equipment.getId())
                .variable("name", equipment.getName())
                .dataSet("ds_paging", equipment.getPagination())
                .dataSet("ds_desktop", equipment.getDesktop())
                .dataSet("laptops", equipment.getLaptops())
                .build();
    }

    @ResponseBody
    @PostMapping("/converter")
    public Equipment converterNormal(@RequestBody Equipment equipment) {
        Pageable pageable = equipment.getPagination().pageable();
        return service.getEquipment(equipment, pageable);
    }
}
