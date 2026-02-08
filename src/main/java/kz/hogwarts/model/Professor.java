package kz.hogwarts.model;

public class Professor extends Person {
    private String subject;
    private Double salary;

    public Professor() {
        super();
    }

    public Professor(String name, int age, Integer houseId, String subject, Double salary) {
        super(name, age, houseId);
        this.subject = subject;
        this.salary = salary;
    }

    public Professor(Integer id, String name, int age, Integer houseId, String subject, Double salary) {
        super(id, name, age, houseId);
        this.subject = subject;
        this.salary = salary;
    }

    @Override
    public String getRole() {
        return "Professor";
    }

    @Override
    public String getPersonType() {
        return "PROFESSOR";
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