package kz.hogwarts.model;

public class House {
    private Integer id;
    private String name;
    private String founder;
    private Integer points;

    public House() {}

    public House(String name, String founder, Integer points) {
        this.name = name;
        this.founder = founder;
        this.points = points;
    }

    public House(Integer id, String name, String founder, Integer points) {
        this.id = id;
        this.name = name;
        this.founder = founder;
        this.points = points;
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

    public String getFounder() {
        return founder;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}