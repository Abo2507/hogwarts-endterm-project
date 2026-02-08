package kz.hogwarts.service;

import kz.hogwarts.model.Course;
import kz.hogwarts.repository.CourseRepository;
import kz.hogwarts.utils.ValidationUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Integer id) {
        ValidationUtils.validatePositive(id, "Course ID");
        return courseRepository.findById(id);
    }

    public Course createCourse(Course course) {
        ValidationUtils.validateNotEmpty(course.getName(), "Course name");

        ValidationUtils.validatePositive(course.getCredits(), "Credits");

        return courseRepository.save(course);
    }

    public Course updateCourse(Integer id, Course course) {
        ValidationUtils.validatePositive(id, "Course ID");
        ValidationUtils.validateNotEmpty(course.getName(), "Course name");
        ValidationUtils.validatePositive(course.getCredits(), "Credits");

        return courseRepository.update(id, course);
    }

    public void deleteCourse(Integer id) {
        ValidationUtils.validatePositive(id, "Course ID");
        courseRepository.delete(id);
    }
}