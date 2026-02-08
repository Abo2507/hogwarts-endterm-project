package kz.hogwarts.patterns.factory;

import kz.hogwarts.model.Person;
import kz.hogwarts.model.Professor;
import kz.hogwarts.model.Student;

/**
 * Factory Pattern: Person Factory
 * Creates instances of Person subclasses (Student, Professor) based on type
 * Returns the base type (Person) to support polymorphism
 */
public class PersonFactory {


    public static Person createPerson(PersonType type, String name, int age, Integer houseId) {
        switch (type) {
            case STUDENT:
                return new Student(name, age, houseId, null, null);
            case PROFESSOR:
                return new Professor(name, age, houseId, null, null);
            default:
                throw new IllegalArgumentException("Unknown person type: " + type);
        }
    }

    public static Person createStudent(String name, int age, Integer houseId, Integer year, String patronus) {
        return new Student(name, age, houseId, year, patronus);
    }

    public static Person createProfessor(String name, int age, Integer houseId, String subject, Double salary) {
        return new Professor(name, age, houseId, subject, salary);
    }
}