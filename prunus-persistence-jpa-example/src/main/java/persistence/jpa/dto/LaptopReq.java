package persistence.jpa.dto;

import lombok.*;
import prunus.persistence.data.pagination.Pagination;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaptopReq extends Pagination {
    private long id;

    private String vendor;

    private int displaySize;

    private boolean deleted;
}
