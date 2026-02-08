package kz.hogwarts.repository;

import kz.hogwarts.exception.DatabaseOperationException;
import kz.hogwarts.exception.ResourceNotFoundException;
import kz.hogwarts.model.Student;
import kz.hogwarts.patterns.singleton.LoggingService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class StudentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final LoggingService logger = LoggingService.getInstance();

    private final RowMapper<Student> studentRowMapper = (rs, rowNum) -> new Student(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getInt("age"),
            (Integer) rs.getObject("house_id"),
            (Integer) rs.getObject("year"),
            rs.getString("patronus")
    );

    public StudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Student> findAll() {
        String sql = "SELECT * FROM persons WHERE person_type = 'STUDENT' ORDER BY name";
        logger.debug("Fetching all students");
        return jdbcTemplate.query(sql, studentRowMapper);
    }

    public Student findById(Integer id) {
        String sql = "SELECT * FROM persons WHERE id = ? AND person_type = 'STUDENT'";
        List<Student> students = jdbcTemplate.query(sql, studentRowMapper, id);
        if (students.isEmpty()) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
        logger.debug("Found student with id: " + id);
        return students.get(0);
    }

    public Student save(Student student) {
        try {
            String sql = "INSERT INTO persons (name, age, person_type, house_id, year, patronus) " +
                    "VALUES (?, ?, 'STUDENT', ?, ?, ?) RETURNING id";

            Integer id = jdbcTemplate.queryForObject(
                    sql,
                    Integer.class,
                    student.getName(),
                    student.getAge(),
                    student.getHouseId(),
                    student.getYear(),
                    student.getPatronus()
            );

            student.setId(id);
            logger.info("Created student with id: " + student.getId());
            return student;

        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to create student", e);
        }
    }


    public Student update(Integer id, Student student) {
        findById(id); // Check if exists

        try {
            String sql = "UPDATE persons SET name = ?, age = ?, house_id = ?, year = ?, patronus = ? " +
                    "WHERE id = ? AND person_type = 'STUDENT'";
            jdbcTemplate.update(sql, student.getName(), student.getAge(), student.getHouseId(),
                    student.getYear(), student.getPatronus(), id);
            student.setId(id);
            logger.info("Updated student with id: " + id);
            return student;
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to update student", e);
        }
    }


    public void delete(Integer id) {
        findById(id); // Check if exists
        String sql = "DELETE FROM persons WHERE id = ? AND person_type = 'STUDENT'";
        jdbcTemplate.update(sql, id);
        logger.info("Deleted student with id: " + id);
    }

    public List<Student> findByHouseId(Integer houseId) {
        String sql = "SELECT * FROM persons WHERE person_type = 'STUDENT' AND house_id = ? ORDER BY name";
        return jdbcTemplate.query(sql, studentRowMapper, houseId);
    }
}