package kz.hogwarts.model;

public class Student extends Person {
    private Integer year;
    private String patronus;

    public Student() {
        super();
    }

    public Student(String name, int age, Integer houseId, Integer year, String patronus) {
        super(name, age, houseId);
        this.year = year;
        this.patronus = patronus;
    }

    public Student(Integer id, String name, int age, Integer houseId, Integer year, String patronus) {
        super(id, name, age, houseId);
        this.year = year;
        this.patronus = patronus;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    @Override
    public String getPersonType() {
        return "STUDENT";
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