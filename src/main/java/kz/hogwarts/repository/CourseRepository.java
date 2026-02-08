package kz.hogwarts.repository;

import kz.hogwarts.exception.DatabaseOperationException;
import kz.hogwarts.exception.DuplicateResourceException;
import kz.hogwarts.exception.ResourceNotFoundException;
import kz.hogwarts.model.Course;
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
public class CourseRepository {

    private final JdbcTemplate jdbcTemplate;
    private final LoggingService logger = LoggingService.getInstance();

    private final RowMapper<Course> courseRowMapper = (rs, rowNum) -> new Course(
            rs.getInt("id"),
            rs.getString("name"),
            (Integer) rs.getObject("professor_id"),
            rs.getInt("credits")
    );

    public CourseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Course> findAll() {
        String sql = "SELECT * FROM courses ORDER BY name";
        logger.debug("Fetching all courses");
        return jdbcTemplate.query(sql, courseRowMapper);
    }

    public Course findById(Integer id) {
        String sql = "SELECT * FROM courses WHERE id = ?";
        List<Course> courses = jdbcTemplate.query(sql, courseRowMapper, id);
        if (courses.isEmpty()) {
            throw new ResourceNotFoundException("Course not found with id: " + id);
        }
        logger.debug("Found course with id: " + id);
        return courses.get(0);
    }

    public Course save(Course course) {
        try {
            String sql = "INSERT INTO courses (name, professor_id, credits) VALUES (?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, course.getName());
                ps.setObject(2, course.getProfessorId());
                ps.setInt(3, course.getCredits());
                return ps;
            }, keyHolder);

            course.setId(keyHolder.getKey().intValue());
            logger.info("Created course with id: " + course.getId());
            return course;
        } catch (Exception e) {
            if (e.getMessage().contains("unique") || e.getMessage().contains("duplicate")) {
                throw new DuplicateResourceException("Course with name '" + course.getName() + "' already exists");
            }
            throw new DatabaseOperationException("Failed to create course", e);
        }
    }

    public Course update(Integer id, Course course) {
        findById(id); // Check if exists


        try {
            String sql = "UPDATE courses SET name = ?, professor_id = ?, credits = ? WHERE id = ?";
            jdbcTemplate.update(sql, course.getName(), course.getProfessorId(), course.getCredits(), id);
            course.setId(id);
            logger.info("Updated course with id: " + id);
            return course;
        } catch (Exception e) {
            if (e.getMessage().contains("unique") || e.getMessage().contains("duplicate")) {
                throw new DuplicateResourceException("Course with name '" + course.getName() + "' already exists");
            }
            throw new DatabaseOperationException("Failed to update course", e);
        }
    }

    public void delete(Integer id) {
        findById(id); // Check if exists
        String sql = "DELETE FROM courses WHERE id = ?";
        jdbcTemplate.update(sql, id);
        logger.info("Deleted course with id: " + id);
    }
}