package kz.hogwarts.service;

import kz.hogwarts.dto.StudentCreateDTO;
import kz.hogwarts.model.Student;
import kz.hogwarts.patterns.factory.PersonFactory;
import kz.hogwarts.patterns.singleton.CacheService;
import kz.hogwarts.repository.StudentRepository;
import kz.hogwarts.utils.ValidationUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private static final String CACHE_KEY_ALL_STUDENTS = "students:all";
    private static final String CACHE_PREFIX_STUDENTS = "students:";

    private final StudentRepository studentRepository;
    private final CacheService cache = CacheService.getInstance();

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * BONUS TASK: Cached method - avoids repeated database queries
     */
    @SuppressWarnings("unchecked")
    public List<Student> getAllStudents() {
        // Try to get from cache first
        Object cached = cache.get(CACHE_KEY_ALL_STUDENTS);
        if (cached != null) {
            return (List<Student>) cached;
        }

        List<Student> students = studentRepository.findAll();

        cache.put(CACHE_KEY_ALL_STUDENTS, students);

        return students;
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

        // Using Factory Pattern to create Student
        Student student = (Student) PersonFactory.createStudent(
                dto.getName(),
                dto.getAge(),
                dto.getHouseId(),
                dto.getYear(),
                dto.getPatronus()
        );

        Student created = studentRepository.save(student);

        cache.evictByPrefix(CACHE_PREFIX_STUDENTS);

        return created;
    }

    public Student updateStudent(Integer id, StudentCreateDTO dto) {
        ValidationUtils.validatePositive(id, "Student ID");
        ValidationUtils.validateNotEmpty(dto.getName(), "Student name");
        ValidationUtils.validatePositive(dto.getAge(), "Age");

        if (dto.getYear() != null) {
            ValidationUtils.validateRange(dto.getYear(), 1, 7, "Year");
        }

        Student student = new Student(
                dto.getName(),
                dto.getAge(),
                dto.getHouseId(),
                dto.getYear(),
                dto.getPatronus()
        );

        Student updated = studentRepository.update(id, student);

        cache.evictByPrefix(CACHE_PREFIX_STUDENTS);

        return updated;
    }

    public void deleteStudent(Integer id) {
        ValidationUtils.validatePositive(id, "Student ID");
        studentRepository.delete(id);


        cache.evictByPrefix(CACHE_PREFIX_STUDENTS);
    }

    public List<Student> getStudentsByHouse(Integer houseId) {
        ValidationUtils.validatePositive(houseId, "House ID");
        return studentRepository.findByHouseId(houseId);
    }
}
