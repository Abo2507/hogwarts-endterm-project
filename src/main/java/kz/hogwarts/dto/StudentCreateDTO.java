package kz.hogwarts.dto;

import jakarta.validation.constraints.*;

public class StudentCreateDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Age is required")
    @Min(value = 11, message = "Student must be at least 11 years old")
    @Max(value = 18, message = "Student cannot be older than 18")
    private Integer age;

    private Integer houseId;

    @Min(value = 1, message = "Year must be between 1 and 7")
    @Max(value = 7, message = "Year must be between 1 and 7")
    private Integer year;

    private String patronus;

    public StudentCreateDTO() {}

    public StudentCreateDTO(String name, Integer age, Integer houseId, Integer year, String patronus) {
        this.name = name;
        this.age = age;
        this.houseId = houseId;
        this.year = year;
        this.patronus = patronus;

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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPatronus() {
        return patronus;
    }

    public void setPatronus(String patronus) {
        this.patronus = patronus;
    }
}