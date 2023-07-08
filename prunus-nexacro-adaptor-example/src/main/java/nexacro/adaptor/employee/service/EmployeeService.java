package nexacro.adaptor.employee.service;

import nexacro.adaptor.employee.domain.Employee;
import nexacro.adaptor.employee.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmployeeService {

    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    /**
     * 사원정보 조회
     * @param id 사원아이디
     * @return 사원정보
     */
    public Employee findById(String id) {
        return employeeMapper.findById(id);
    }
    public Map<String, Object> findMapById(String id) {
        return employeeMapper.findMapById(id);
    }
}
