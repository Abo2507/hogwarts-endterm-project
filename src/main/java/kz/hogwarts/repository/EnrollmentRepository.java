package kz.hogwarts.repository;

import kz.hogwarts.exception.DatabaseOperationException;
import kz.hogwarts.exception.DuplicateResourceException;
import kz.hogwarts.exception.ResourceNotFoundException;
import kz.hogwarts.model.Enrollment;
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
public class EnrollmentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final LoggingService logger = LoggingService.getInstance();

    private final RowMapper<Enrollment> enrollmentRowMapper = (rs, rowNum) -> new Enrollment(
            rs.getInt("id"),
            rs.getInt("student_id"),
            rs.getInt("course_id"),
            rs.getString("grade")
    );

    public EnrollmentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Enrollment> findAll() {
        String sql = "SELECT * FROM enrollments ORDER BY id";
        logger.debug("Fetching all enrollments");
        return jdbcTemplate.query(sql, enrollmentRowMapper);
    }

    public Enrollment findById(Integer id) {
        String sql = "SELECT * FROM enrollments WHERE id = ?";
        List<Enrollment> enrollments = jdbcTemplate.query(sql, enrollmentRowMapper, id);
        if (enrollments.isEmpty()) {
            throw new ResourceNotFoundException("Enrollment not found with id: " + id);
        }
        logger.debug("Found enrollment with id: " + id);
        return enrollments.get(0);
    }

    public Enrollment save(Enrollment enrollment) {
        try {
            String sql = "INSERT INTO enrollments (student_id, course_id, grade) VALUES (?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, enrollment.getStudentId());
                ps.setInt(2, enrollment.getCourseId());
                ps.setString(3, enrollment.getGrade());
                return ps;
            }, keyHolder);

            enrollment.setId(keyHolder.getKey().intValue());
            logger.info("Created enrollment with id: " + enrollment.getId());
            return enrollment;
        } catch (Exception e) {
            if (e.getMessage().contains("unique") || e.getMessage().contains("duplicate")) {
                throw new DuplicateResourceException(
                        "Student is already enrolled in this course");
            }
            throw new DatabaseOperationException("Failed to create enrollment", e);
        }
    }

    public Enrollment update(Integer id, Enrollment enrollment) {
        findById(id); // Check if exists

        try {
            String sql = "UPDATE enrollments SET student_id = ?, course_id = ?, grade = ? WHERE id = ?";
            jdbcTemplate.update(sql, enrollment.getStudentId(), enrollment.getCourseId(),
                    enrollment.getGrade(), id);
            enrollment.setId(id);
            logger.info("Updated enrollment with id: " + id);
            return enrollment;
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to update enrollment", e);
        }
    }


    public void delete(Integer id) {
        findById(id); // Check if exists
        String sql = "DELETE FROM enrollments WHERE id = ?";
        jdbcTemplate.update(sql, id);
        logger.info("Deleted enrollment with id: " + id);
    }

    public List<Enrollment> findByStudentId(Integer studentId) {
        String sql = "SELECT * FROM enrollments WHERE student_id = ?";
        return jdbcTemplate.query(sql, enrollmentRowMapper, studentId);
    }

    public List<Enrollment> findByCourseId(Integer courseId) {
        String sql = "SELECT * FROM enrollments WHERE course_id = ?";
        return jdbcTemplate.query(sql, enrollmentRowMapper, courseId);
    }
}