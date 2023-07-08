package datasource.service;

import datasource.Pair;
import datasource.entity.Student;
import datasource.entity.Teacher;
import org.springframework.stereotype.Service;
import prunus.datasource.annotation.LocalTransactional;

import java.util.List;

@Service
public class SchoolService {
    private final StudentService studentService;
    private final TeacherService teacherService;

    public SchoolService(StudentService studentService, TeacherService teacherService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    public Pair save(Pair pair) {
        Student student = studentService.save(pair.getStudent());
        Teacher teacher = teacherService.save(pair.getTeacher());
        pair.setStudent(student);
        pair.setTeacher(teacher);
        return pair;
    }

    @LocalTransactional
    public Pair saveWithTx(Pair pair) {
        studentService.save(pair.getStudent());
        teacherService.saveWithTx(pair.getTeacher());
        "".substring(2);
        return pair;
    }

    public List<Student> findAllStudent() {
        return studentService.findAll();
    }

    public List<Teacher> findAllTeacher() {
        return teacherService.findAll();
    }
}
