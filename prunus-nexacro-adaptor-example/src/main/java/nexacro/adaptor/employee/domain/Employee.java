package nexacro.adaptor.employee.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * 사원 Entity
 */
@Getter @Setter
public class Employee {
    private String id;

    private String employeeName;

    private Integer salary;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd", timezone = "Asia/Seoul")
    private LocalDate hireDate;

    private Integer married;

    private String email;

    private String fileBucketId;

    private String backNumber;

    private String departmentId;

    private Department department;

    private List<Education> educations;
}
