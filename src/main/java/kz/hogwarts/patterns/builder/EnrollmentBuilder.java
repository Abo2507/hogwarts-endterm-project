package kz.hogwarts.patterns.builder;

import kz.hogwarts.model.Enrollment;

/**
 * Builder Pattern: Enrollment Builder
 * Provides a fluent interface for constructing Enrollment objects with optional fields
 * Supports method chaining for easy object creation
 */
public class EnrollmentBuilder {


    private Integer id;
    private Integer studentId;
    private Integer courseId;
    private String grade;

    public EnrollmentBuilder() {}

    public EnrollmentBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public EnrollmentBuilder withStudentId(Integer studentId) {
        this.studentId = studentId;
        return this;
    }

    public EnrollmentBuilder withCourseId(Integer courseId) {
        this.courseId = courseId;
        return this;
    }

    public EnrollmentBuilder withGrade(String grade) {
        this.grade = grade;
        return this;
    }

    public Enrollment build() {
        if (studentId == null || courseId == null) {
            throw new IllegalStateException("Student ID and Course ID are required");
        }

        if (id != null) {
            return new Enrollment(id, studentId, courseId, grade);
        } else {
            return new Enrollment(studentId, courseId, grade);
        }
    }
}