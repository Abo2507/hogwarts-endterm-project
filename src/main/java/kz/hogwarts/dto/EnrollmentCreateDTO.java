package kz.hogwarts.dto;

import jakarta.validation.constraints.*;

public class EnrollmentCreateDTO {

    @NotNull(message = "Student ID is required")
    private Integer studentId;

    @NotNull(message = "Course ID is required")
    private Integer courseId;

    @Pattern(regexp = "^[OEA]$", message = "Grade must be O, E, or A")
    private String grade;

    public EnrollmentCreateDTO() {}


    public EnrollmentCreateDTO(Integer studentId, Integer courseId, String grade) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.grade = grade;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}