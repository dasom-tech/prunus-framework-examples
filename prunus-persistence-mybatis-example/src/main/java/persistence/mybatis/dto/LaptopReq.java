package persistence.mybatis.dto;

import lombok.Getter;
import lombok.Setter;
import prunus.persistence.data.pagination.Pagination;

@Getter
@Setter
public class LaptopReq extends Pagination {
    private long id;

    private String vendor;

    private int displaySize;

    private boolean deleted;
}
