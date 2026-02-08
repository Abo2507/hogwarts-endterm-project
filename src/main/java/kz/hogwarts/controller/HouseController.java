package kz.hogwarts.controller;

import jakarta.validation.Valid;
import kz.hogwarts.model.House;
import kz.hogwarts.service.HouseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/houses")
public class HouseController {

    private final HouseService houseService;

    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }


    @GetMapping
    public ResponseEntity<List<House>> getAllHouses() {
        List<House> houses = houseService.getAllHouses();
        return ResponseEntity.ok(houses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<House> getHouseById(@PathVariable Integer id) {
        House house = houseService.getHouseById(id);
        return ResponseEntity.ok(house);
    }

    @PostMapping
    public ResponseEntity<House> createHouse(@Valid @RequestBody House house) {
        House createdHouse = houseService.createHouse(house);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHouse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<House> updateHouse(
            @PathVariable Integer id,
            @Valid @RequestBody House house) {
        House updatedHouse = houseService.updateHouse(id, house);
        return ResponseEntity.ok(updatedHouse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHouse(@PathVariable Integer id) {
        houseService.deleteHouse(id);
        return ResponseEntity.noContent().build();
    }
}