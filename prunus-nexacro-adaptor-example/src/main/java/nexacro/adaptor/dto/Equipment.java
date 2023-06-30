package nexacro.adaptor.dto;

import lombok.*;
import prunus.nexacro.adaptor.resolver.annotation.DataSetParam;
import prunus.persistence.data.pagination.Pagination;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipment {
    private String id;
    private String name;
    private int seq;
    @DataSetParam(name="ds_paging")
    private Pagination pagination;
    @DataSetParam("ds_desktop")
    private Desktop desktop;
    private List<Laptop> laptops;
    @DataSetParam(name="normal", required = false)
    private List<Laptop> normal;
    @DataSetParam(name="inserted", required = false)
    private List<Laptop> inserted;
    @DataSetParam(name="updated", required = false)
    private List<Laptop> updated;
    @DataSetParam(name="deleted", required = false)
    private List<Laptop> deleted;
}
