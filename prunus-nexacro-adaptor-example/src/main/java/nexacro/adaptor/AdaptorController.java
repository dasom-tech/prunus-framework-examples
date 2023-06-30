package nexacro.adaptor;

import com.nexacro.java.xapi.data.DataSet;
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
import java.util.stream.Collectors;

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
    public NexacroResult resolver(
            @VariableParam("id") String id,
            @VariableParam("name") String name,
            @VariableParam(name="seq", required = false) int seq,
            @DataSetParam("laptops") List<Laptop> laptops,
            @DataSetParam(name="ds_desktop", required = false) Desktop desktop,
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

    @PostMapping("/resolver/save")
    public NexacroResult resolverSave(@DataSetParam("laptops") List<Laptop> laptops) {
        List<Laptop> normal = laptops.stream().filter(laptop -> laptop.getRowType() == DataSet.ROW_TYPE_NORMAL).collect(Collectors.toList());
        List<Laptop> inserted = laptops.stream().filter(laptop -> laptop.getRowType() == DataSet.ROW_TYPE_INSERTED).collect(Collectors.toList());
        List<Laptop> updated = laptops.stream().filter(laptop -> laptop.getRowType() == DataSet.ROW_TYPE_UPDATED).collect(Collectors.toList());
        List<Laptop> deleted = laptops.stream().filter(laptop -> laptop.getRowType() == DataSet.ROW_TYPE_DELETED).collect(Collectors.toList());
        return NexacroResult.builder()
                .dataSet("normal", normal)
                .dataSet("inserted", inserted)
                .dataSet("updated", updated)
                .dataSet("deleted", deleted)
                .build();
    }

    @ResponseBody
    @PostMapping("/converter")
    public Equipment converterNormal(@RequestBody Equipment equipment) {
        Pageable pageable = equipment.getPagination().pageable();
        return service.getEquipment(equipment, pageable);
    }

    @ResponseBody
    @PostMapping("/converter/save")
    public Equipment converterSave(@RequestBody Equipment equipment) {
        List<Laptop> normal = equipment.getLaptops().stream().filter(laptop -> laptop.getRowType() == DataSet.ROW_TYPE_NORMAL).collect(Collectors.toList());
        List<Laptop> inserted = equipment.getLaptops().stream().filter(laptop -> laptop.getRowType() == DataSet.ROW_TYPE_INSERTED).collect(Collectors.toList());
        List<Laptop> updated = equipment.getLaptops().stream().filter(laptop -> laptop.getRowType() == DataSet.ROW_TYPE_UPDATED).collect(Collectors.toList());
        List<Laptop> deleted = equipment.getLaptops().stream().filter(laptop -> laptop.getRowType() == DataSet.ROW_TYPE_DELETED).collect(Collectors.toList());
        return Equipment.builder()
                .normal(normal)
                .inserted(inserted)
                .updated(updated)
                .deleted(deleted)
                .build();
    }
}
