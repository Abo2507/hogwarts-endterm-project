package kz.hogwarts.service;

import kz.hogwarts.model.House;
import kz.hogwarts.patterns.singleton.CacheService;
import kz.hogwarts.repository.HouseRepository;
import kz.hogwarts.utils.ValidationUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseService {

    private static final String CACHE_KEY_ALL_HOUSES = "houses:all";
    private static final String CACHE_PREFIX_HOUSES = "houses:";

    private final HouseRepository houseRepository;
    private final CacheService cache = CacheService.getInstance();

    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    /**
     * BONUS TASK: Cached method - houses rarely change
     */
    @SuppressWarnings("unchecked")
    public List<House> getAllHouses() {
        // Try cache first
        Object cached = cache.get(CACHE_KEY_ALL_HOUSES);
        if (cached != null) {
            return (List<House>) cached;
        }

        List<House> houses = houseRepository.findAll();
        cache.put(CACHE_KEY_ALL_HOUSES, houses);

        return houses;
    }

    public House getHouseById(Integer id) {
        ValidationUtils.validatePositive(id, "House ID");

        String cacheKey = CACHE_PREFIX_HOUSES + id;
        Object cached = cache.get(cacheKey);
        if (cached != null) {
            return (House) cached;
        }

        House house = houseRepository.findById(id);
        cache.put(cacheKey, house);

        return house;
    }

    public House createHouse(House house) {
        ValidationUtils.validateNotEmpty(house.getName(), "House name");
        ValidationUtils.validateNotEmpty(house.getFounder(), "Founder");

        if (house.getPoints() == null) {
            house.setPoints(0);
        }

        House created = houseRepository.save(house);

        cache.evictByPrefix(CACHE_PREFIX_HOUSES);

        return created;
    }

    public House updateHouse(Integer id, House house) {
        ValidationUtils.validatePositive(id, "House ID");
        ValidationUtils.validateNotEmpty(house.getName(), "House name");
        ValidationUtils.validateNotEmpty(house.getFounder(), "Founder");

        House updated = houseRepository.update(id, house);

        cache.evictByPrefix(CACHE_PREFIX_HOUSES);

        return updated;
    }

    public void deleteHouse(Integer id) {
        ValidationUtils.validatePositive(id, "House ID");
        houseRepository.delete(id);

        cache.evictByPrefix(CACHE_PREFIX_HOUSES);
    }
}
