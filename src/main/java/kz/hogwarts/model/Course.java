package kz.hogwarts.model;

public class Course {
    private Integer id;
    private String name;
    private Integer professorId;
    private Integer credits;

    public Course() {}

    public Course(String name, Integer professorId, Integer credits) {
        this.name = name;
        this.professorId = professorId;
        this.credits = credits;
    }

    public Course(Integer id, String name, Integer professorId, Integer credits) {
        this.id = id;
        this.name = name;
        this.professorId = professorId;
        this.credits = credits;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Integer professorId) {
        this.professorId = professorId;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }
}