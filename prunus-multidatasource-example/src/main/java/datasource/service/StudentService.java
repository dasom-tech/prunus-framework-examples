package datasource.service;

import datasource.controller.DataSourceSelector;
import datasource.entity.Student;
import datasource.repository.StudentRepository;
import org.springframework.stereotype.Service;
import prunus.datasource.annotation.DataSourceName;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student save(Student student) {
        return repository.save(student);
    }

    public List<Student> findAll() {
        return repository.findAll();
    }

    @DataSourceName("#header.ds")
    public List<Student> findAllByHeader() {
        return repository.findAll();
    }

    @DataSourceName("#session.ds")
    public List<Student> findAllBySession() {
        return repository.findAll();
    }

    @DataSourceName("#selector.ds")
    public List<Student> findAllBySpel(DataSourceSelector selector) {
        return repository.findAll();
    }
}
