package nexacro.adaptor.employee.mapper;

import nexacro.adaptor.employee.domain.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface EmployeeMapper {

    Employee findById(String id);

    Map<String, Object> findMapById(String id);
}
