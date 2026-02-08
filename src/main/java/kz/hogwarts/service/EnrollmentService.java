package kz.hogwarts.service;

import kz.hogwarts.dto.EnrollmentCreateDTO;
import kz.hogwarts.model.Enrollment;
import kz.hogwarts.patterns.builder.EnrollmentBuilder;
import kz.hogwarts.repository.EnrollmentRepository;
import kz.hogwarts.utils.ValidationUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    public Enrollment getEnrollmentById(Integer id) {
        ValidationUtils.validatePositive(id, "Enrollment ID");
        return enrollmentRepository.findById(id);
    }

    public Enrollment createEnrollment(EnrollmentCreateDTO dto) {
        ValidationUtils.validatePositive(dto.getStudentId(), "Student ID");
        ValidationUtils.validatePositive(dto.getCourseId(), "Course ID");



        Enrollment enrollment = new EnrollmentBuilder()
                .withStudentId(dto.getStudentId())
                .withCourseId(dto.getCourseId())
                .withGrade(dto.getGrade())
                .build();

        return enrollmentRepository.save(enrollment);
    }

    public Enrollment updateEnrollment(Integer id, EnrollmentCreateDTO dto) {
        ValidationUtils.validatePositive(id, "Enrollment ID");
        ValidationUtils.validatePositive(dto.getStudentId(), "Student ID");
        ValidationUtils.validatePositive(dto.getCourseId(), "Course ID");

        Enrollment enrollment = new EnrollmentBuilder()
                .withId(id)
                .withStudentId(dto.getStudentId())
                .withCourseId(dto.getCourseId())
                .withGrade(dto.getGrade())
                .build();

        return enrollmentRepository.update(id, enrollment);
    }

    public void deleteEnrollment(Integer id) {
        ValidationUtils.validatePositive(id, "Enrollment ID");
        enrollmentRepository.delete(id);
    }

    public List<Enrollment> getEnrollmentsByStudent(Integer studentId) {
        ValidationUtils.validatePositive(studentId, "Student ID");
        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Enrollment> getEnrollmentsByCourse(Integer courseId) {
        ValidationUtils.validatePositive(courseId, "Course ID");
        return enrollmentRepository.findByCourseId(courseId);
    }
}