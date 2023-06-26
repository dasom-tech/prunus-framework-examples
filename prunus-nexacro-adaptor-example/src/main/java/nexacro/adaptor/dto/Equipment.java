package nexacro.adaptor.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;
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
    @JsonAlias("ds_paging")
    private Pagination pagination;
    @JsonAlias("ds_desktop")
    private Desktop desktop;
    private List<Laptop> laptops;
}
