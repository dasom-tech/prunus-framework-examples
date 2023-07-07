package datasource.controller;

import datasource.Pair;
import datasource.entity.Student;
import datasource.entity.Teacher;
import datasource.service.SchoolService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SchoolController {
    private final SchoolService service;

    public SchoolController(SchoolService service) {
        this.service = service;
    }

    @PostMapping("/schools")
    public Pair save(@RequestBody Pair pair) {
        return service.save(pair);
    }

    @PostMapping("/schools/tx")
    public Pair saveWithTx(@RequestBody Pair pair) {
        return service.saveWithTx(pair);
    }

    @GetMapping("/students")
    public List<Student> findAllStudent() {
        return service.findAllStudent();
    }

    @GetMapping("/teachers")
    public List<Teacher> findAllTeacher() {
        return service.findAllTeacher();
    }
}
