package kz.hogwarts.service;

import kz.hogwarts.model.House;
import kz.hogwarts.repository.HouseRepository;
import kz.hogwarts.utils.ValidationUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseService {

    private final HouseRepository houseRepository;

    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    public List<House> getAllHouses() {
        return houseRepository.findAll();
    }

    public House getHouseById(Integer id) {
        ValidationUtils.validatePositive(id, "House ID");
        return houseRepository.findById(id);
    }

    public House createHouse(House house) {
        ValidationUtils.validateNotEmpty(house.getName(), "House name");
        ValidationUtils.validateNotEmpty(house.getFounder(), "Founder");

        if (house.getPoints() == null) {
            house.setPoints(0);
        }

        return houseRepository.save(house);
    }


    public House updateHouse(Integer id, House house) {
        ValidationUtils.validatePositive(id, "House ID");
        ValidationUtils.validateNotEmpty(house.getName(), "House name");
        ValidationUtils.validateNotEmpty(house.getFounder(), "Founder");

        return houseRepository.update(id, house);
    }

    public void deleteHouse(Integer id) {
        ValidationUtils.validatePositive(id, "House ID");
        houseRepository.delete(id);
    }
}