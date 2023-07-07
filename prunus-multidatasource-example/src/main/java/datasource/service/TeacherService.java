package datasource.service;

import datasource.entity.Teacher;
import datasource.repository.TeacherRepository;
import org.springframework.stereotype.Service;
import prunus.datasource.annotation.DataSourceName;
import prunus.datasource.annotation.LocalTransactional;
import prunus.datasource.tx.LocalPropagation;

import java.util.List;

@Service
@DataSourceName("teacher")
public class TeacherService {
    private final TeacherRepository repository;

    public TeacherService(TeacherRepository repository) {
        this.repository = repository;
    }

    public Teacher save(Teacher teacher) {
        return repository.save(teacher);
    }

    @LocalTransactional(propagation = LocalPropagation.REQUIRES_NEW)
    public Teacher saveWithTx(Teacher teacher) {
        return repository.save(teacher);
    }

    public List<Teacher> findAll() {
        return repository.findAll();
    }
}
