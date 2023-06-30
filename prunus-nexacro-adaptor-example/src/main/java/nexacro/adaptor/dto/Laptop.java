package nexacro.adaptor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import prunus.nexacro.adaptor.data.DataSetRowType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Laptop extends DataSetRowType {
    private String id;
    private String vendor;
    private int displaySize;
}
