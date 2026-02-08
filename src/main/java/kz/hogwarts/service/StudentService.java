package kz.hogwarts.service;

import kz.hogwarts.dto.StudentCreateDTO;
import kz.hogwarts.model.Student;
import kz.hogwarts.patterns.factory.PersonFactory;
import kz.hogwarts.repository.StudentRepository;
import kz.hogwarts.utils.ValidationUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Integer id) {
        ValidationUtils.validatePositive(id, "Student ID");
        return studentRepository.findById(id);
    }

    public Student createStudent(StudentCreateDTO dto) {
        ValidationUtils.validateNotEmpty(dto.getName(), "Student name");
        ValidationUtils.validatePositive(dto.getAge(), "Age");

        if (dto.getYear() != null) {
            ValidationUtils.validateRange(dto.getYear(), 1, 7, "Year");
        }


        Student student = (Student) PersonFactory.createStudent(
                dto.getName(),
                dto.getAge(),
                dto.getHouseId(),
                dto.getYear(),
                dto.getPatronus()
        );

        return studentRepository.save(student);
    }

    public Student updateStudent(Integer id, StudentCreateDTO dto) {
        ValidationUtils.validatePositive(id, "Student ID");
        ValidationUtils.validateNotEmpty(dto.getName(), "Student name");
        ValidationUtils.validatePositive(dto.getAge(), "Age");

        if (dto.getYear() != null) {
            ValidationUtils.validateRange(dto.getYear(), 1, 7, "Year");
        }

        Student student = (Student) PersonFactory.createStudent(
                dto.getName(),
                dto.getAge(),
                dto.getHouseId(),
                dto.getYear(),
                dto.getPatronus()
        );

        return studentRepository.update(id, student);

    }

    public void deleteStudent(Integer id) {
        ValidationUtils.validatePositive(id, "Student ID");
        studentRepository.delete(id);
    }

    public List<Student> getStudentsByHouse(Integer houseId) {
        ValidationUtils.validatePositive(houseId, "House ID");
        return studentRepository.findByHouseId(houseId);
    }
}