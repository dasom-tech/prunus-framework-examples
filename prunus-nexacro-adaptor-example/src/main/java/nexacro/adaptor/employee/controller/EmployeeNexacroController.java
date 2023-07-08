package nexacro.adaptor.employee.controller;

import com.nexacro.uiadapter.spring.core.annotation.VariableParam;
import com.nexacro.uiadapter.spring.core.data.NexacroResult;
import lombok.extern.slf4j.Slf4j;
import nexacro.adaptor.employee.domain.Employee;
import nexacro.adaptor.employee.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/employee")
public class EmployeeNexacroController {

    private final EmployeeService employeeService;

    public EmployeeNexacroController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * 사원정보 조회
     * @param employeeId 사원아이디
     * @return Nexacro XML Data
     */
    @PostMapping
    public NexacroResult findEmployee(@VariableParam(name="employeeId") String employeeId) {
        Employee employee = employeeService.findById(employeeId);
        return NexacroResult.builder()
                .dataSet("ds_employee", employee)
                .dataSet("ds_department", employee.getDepartment())
                .dataSet("ds_education", employee.getEducations())
                .build();
    }

    @PostMapping("/error")
    public NexacroResult findEmployeeError(@VariableParam(name="employeeId") String employeeId) {
        Map<String, Object> employee = employeeService.findMapById(employeeId);
        return NexacroResult.builder()
                .dataSet("ds_employee", employee)
                .dataSet("ds_department", employee.get("department"))
                .dataSet("ds_education", employee.get("education"))
                .build();
    }
}
