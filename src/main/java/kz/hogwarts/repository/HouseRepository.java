package kz.hogwarts.repository;

import kz.hogwarts.exception.DatabaseOperationException;
import kz.hogwarts.exception.DuplicateResourceException;
import kz.hogwarts.exception.ResourceNotFoundException;
import kz.hogwarts.model.House;
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
public class HouseRepository {

    private final JdbcTemplate jdbcTemplate;
    private final LoggingService logger = LoggingService.getInstance();

    private final RowMapper<House> houseRowMapper = (rs, rowNum) -> new House(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("founder"),
            rs.getInt("points")
    );

    public HouseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<House> findAll() {
        String sql = "SELECT * FROM houses ORDER BY name";
        logger.debug("Fetching all houses");
        return jdbcTemplate.query(sql, houseRowMapper);
    }

    public House findById(Integer id) {
        String sql = "SELECT * FROM houses WHERE id = ?";
        List<House> houses = jdbcTemplate.query(sql, houseRowMapper, id);
        if (houses.isEmpty()) {
            throw new ResourceNotFoundException("House not found with id: " + id);
        }
        logger.debug("Found house with id: " + id);
        return houses.get(0);
    }

    public House save(House house) {
        try {
            String sql = "INSERT INTO houses (name, founder, points) VALUES (?, ?, ?) RETURNING id";

            Integer id = jdbcTemplate.queryForObject(
                    sql,
                    Integer.class,
                    house.getName(),
                    house.getFounder(),
                    house.getPoints() != null ? house.getPoints() : 0
            );

            house.setId(id);
            logger.info("Created house with id: " + house.getId());
            return house;

        } catch (Exception e) {
            if (e.getMessage() != null && (e.getMessage().contains("unique") || e.getMessage().contains("duplicate"))) {
                throw new DuplicateResourceException("House with name '" + house.getName() + "' already exists");
            }
            throw new DatabaseOperationException("Failed to create house", e);
        }
    }


    public House update(Integer id, House house) {
        findById(id); // Check if exists

        try {
            String sql = "UPDATE houses SET name = ?, founder = ?, points = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    house.getName(),
                    house.getFounder(),
                    house.getPoints() != null ? house.getPoints() : 0,
                    id
            );
            house.setId(id);
            logger.info("Updated house with id: " + id);
            return house;
        } catch (Exception e) {
            if (e.getMessage().contains("unique") || e.getMessage().contains("duplicate")) {
                throw new DuplicateResourceException("House with name '" + house.getName() + "' already exists");
            }
            throw new DatabaseOperationException("Failed to update house", e);
        }
    }

    public void delete(Integer id) {
        findById(id); // Check if exists
        String sql = "DELETE FROM houses WHERE id = ?";
        jdbcTemplate.update(sql, id);
        logger.info("Deleted house with id: " + id);
    }
}