package nexacro.adaptor.employee.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 사원학력 Entity
 */
@Getter @Setter
public class Education {
    private Long id;

    private String employeeId;

    private String schoolName;

    private String graduationYear;

    private String graduationType;

    private Float grades;
}
