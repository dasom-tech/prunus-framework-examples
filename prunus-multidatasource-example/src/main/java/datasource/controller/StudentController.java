package datasource.controller;

import datasource.entity.Student;
import datasource.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping("/students/header")
    public List<Student> findAllByHeader() {
        return service.findAllByHeader();
    }

    @GetMapping("/students/session")
    public List<Student> findAllByHeader(HttpServletRequest request) {
        request.getSession().setAttribute("ds", "student");
        return service.findAllBySession();
    }

    @GetMapping("/students/spel")
    public List<Student> findAllBySpel(DataSourceSelector selector) {
        return service.findAllBySpel(selector);
    }
}
