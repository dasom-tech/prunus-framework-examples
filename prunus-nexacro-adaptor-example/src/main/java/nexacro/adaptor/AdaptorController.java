package nexacro.adaptor;

import com.nexacro.uiadapter.spring.core.annotation.ParamDataSet;
import com.nexacro.uiadapter.spring.core.annotation.ParamVariable;
import com.nexacro.uiadapter.spring.core.data.NexacroResult;
import lombok.SneakyThrows;
import nexacro.adaptor.dto.Desktop;
import nexacro.adaptor.dto.Equipment;
import nexacro.adaptor.dto.Laptop;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @ResponseBody
    @PostMapping("/converter/normal")
    public Equipment converterNormal(@RequestBody Equipment equipment) {
        Pageable pageable = equipment.getPagination().pageable();
        return service.normal(equipment, pageable);
    }

    @SneakyThrows
    @PostMapping("/resolver/normal")
    public NexacroResult resolverNormal(
            @ParamVariable(name="id") String id,
            @ParamVariable(name="name") String name,
            @ParamVariable(name="seq") int seq,
            @ParamDataSet(name="ds_paging") Pagination pagination,
            @ParamDataSet(name="ds_desktop") Desktop desktop,
            @ParamDataSet(name="laptops") List<Laptop> laptops) {
        Pageable pageable = pagination.pageable();
        Equipment equipment = service.normal(id, name, desktop, laptops, pageable);
        NexacroResult result = null;
        if (equipment != null) {
            result = new NexacroResult();
            result.addVariable("id", equipment.getId());
            result.addVariable("name", equipment.getName());
            result.addDataSet("ds_paging", equipment.getPagination());
            result.addDataSet("ds_desktop", equipment.getDesktop());
            result.addDataSet("laptops", equipment.getLaptops());
        }
        return result;
    }
}
