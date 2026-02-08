package kz.hogwarts.dto;

import jakarta.validation.constraints.*;

public class ProfessorCreateDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Age is required")
    @Min(value = 25, message = "Professor must be at least 25 years old")
    private Integer age;

    private Integer houseId;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotNull(message = "Salary is required")
    @Min(value = 30000, message = "Salary must be at least 30000")
    private Double salary;

    public ProfessorCreateDTO() {}

    public ProfessorCreateDTO(String name, Integer age, Integer houseId, String subject, Double salary) {
        this.name = name;
        this.age = age;
        this.houseId = houseId;
        this.subject = subject;
        this.salary = salary;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getHouseId() {
        return houseId;
    }

    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}