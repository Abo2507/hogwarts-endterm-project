package kz.hogwarts.patterns.singleton;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheService {

    private static CacheService instance;
    private final Map<String, CacheEntry> cache;
    private final LoggingService logger = LoggingService.getInstance();

    private CacheService() {
        this.cache = new ConcurrentHashMap<>();
        logger.info("CacheService initialized");
    }

    public static synchronized CacheService getInstance() {
        if (instance == null) {
            instance = new CacheService();
        }
        return instance;
    }

    public void put(String key, Object data) {
        CacheEntry entry = new CacheEntry(data, LocalDateTime.now());
        cache.put(key, entry);
        logger.debug("Cache PUT: " + key + " (size: " + cache.size() + ")");
    }

    public Object get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry != null) {
            logger.debug("Cache HIT: " + key);
            return entry.getData();
        }
        logger.debug("Cache MISS: " + key);
        return null;
    }

    public boolean contains(String key) {
        return cache.containsKey(key);
    }

    public void evict(String key) {
        cache.remove(key);
        logger.debug("Cache EVICT: " + key);
    }

    public void evictByPrefix(String prefix) {
        int count = 0;
        for (String key : cache.keySet()) {
            if (key.startsWith(prefix)) {
                cache.remove(key);
                count++;
            }
        }
        logger.info("Cache EVICT BY PREFIX: " + prefix + " (" + count + " entries removed)");
    }

    public void clear() {
        int size = cache.size();
        cache.clear();
        logger.info("Cache CLEARED (" + size + " entries removed)");
    }

    public int size() {
        return cache.size();
    }

    public String getStats() {
        return String.format("Cache Stats - Size: %d entries, Keys: %s",
                cache.size(), cache.keySet());
    }

    private static class CacheEntry {
        private final Object data;
        private final LocalDateTime cachedAt;

        public CacheEntry(Object data, LocalDateTime cachedAt) {
            this.data = data;
            this.cachedAt = cachedAt;
        }

        public Object getData() {
            return data;
        }

        public LocalDateTime getCachedAt() {
            return cachedAt;
        }
    }
}
