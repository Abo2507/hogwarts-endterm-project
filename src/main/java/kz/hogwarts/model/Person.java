package kz.hogwarts.model;

public abstract class Person {
    private Integer id;
    private String name;
    private int age;
    private Integer houseId;

    public Person() {}

    public Person(String name, int age, Integer houseId) {
        this.name = name;
        this.age = age;
        this.houseId = houseId;
    }

    public Person(Integer id, String name, int age, Integer houseId) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.houseId = houseId;
    }

    public abstract String getRole();
    public abstract String getPersonType();

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Integer getHouseId() {
        return houseId;
    }

    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }
}