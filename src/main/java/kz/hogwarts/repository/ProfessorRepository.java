package kz.hogwarts.repository;

import kz.hogwarts.exception.DatabaseOperationException;
import kz.hogwarts.exception.ResourceNotFoundException;
import kz.hogwarts.model.Professor;
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
public class ProfessorRepository {

    private final JdbcTemplate jdbcTemplate;
    private final LoggingService logger = LoggingService.getInstance();

    private final RowMapper<Professor> professorRowMapper = (rs, rowNum) -> new Professor(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getInt("age"),
            (Integer) rs.getObject("house_id"),
            rs.getString("subject"),
            rs.getDouble("salary")
    );

    public ProfessorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Professor> findAll() {
        String sql = "SELECT * FROM persons WHERE person_type = 'PROFESSOR' ORDER BY name";
        logger.debug("Fetching all professors");
        return jdbcTemplate.query(sql, professorRowMapper);
    }

    public Professor findById(Integer id) {
        String sql = "SELECT * FROM persons WHERE id = ? AND person_type = 'PROFESSOR'";
        List<Professor> professors = jdbcTemplate.query(sql, professorRowMapper, id);
        if (professors.isEmpty()) {
            throw new ResourceNotFoundException("Professor not found with id: " + id);
        }
        logger.debug("Found professor with id: " + id);
        return professors.get(0);
    }

    public Professor save(Professor professor) {
        try {
            String sql = "INSERT INTO persons (name, age, person_type, house_id, subject, salary) " +
                    "VALUES (?, ?, 'PROFESSOR', ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, professor.getName());
                ps.setInt(2, professor.getAge());
                ps.setObject(3, professor.getHouseId());
                ps.setString(4, professor.getSubject());
                ps.setDouble(5, professor.getSalary());
                return ps;
            }, keyHolder);

            professor.setId(keyHolder.getKey().intValue());
            logger.info("Created professor with id: " + professor.getId());
            return professor;
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to create professor", e);
        }
    }

    public Professor update(Integer id, Professor professor) {
        findById(id); // Check if exists


        try {
            String sql = "UPDATE persons SET name = ?, age = ?, house_id = ?, subject = ?, salary = ? " +
                    "WHERE id = ? AND person_type = 'PROFESSOR'";
            jdbcTemplate.update(sql, professor.getName(), professor.getAge(), professor.getHouseId(),
                    professor.getSubject(), professor.getSalary(), id);
            professor.setId(id);
            logger.info("Updated professor with id: " + id);
            return professor;
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to update professor", e);
        }
    }

    public void delete(Integer id) {
        findById(id); // Check if exists
        String sql = "DELETE FROM persons WHERE id = ? AND person_type = 'PROFESSOR'";
        jdbcTemplate.update(sql, id);
        logger.info("Deleted professor with id: " + id);
    }
}