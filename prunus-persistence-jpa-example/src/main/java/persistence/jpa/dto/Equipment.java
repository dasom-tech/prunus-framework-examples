package persistence.jpa.dto;

import lombok.Getter;
import lombok.Setter;
import prunus.persistence.data.pagination.Pagination;

@Getter
@Setter
public class Equipment {
    private String vendor;
    private Pagination pagination;
}
