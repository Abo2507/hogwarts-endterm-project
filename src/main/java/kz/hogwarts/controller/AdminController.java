package kz.hogwarts.controller;

import kz.hogwarts.patterns.singleton.CacheService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * BONUS TASK: Admin endpoints for manual cache management
 */
@RestController
@RequestMapping("/api/admin/cache")
public class AdminController {

    private final CacheService cache = CacheService.getInstance();

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getCacheStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("cacheSize", cache.size());
        stats.put("details", cache.getStats());
        stats.put("message", "Cache statistics retrieved successfully");
        return ResponseEntity.ok(stats);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearCache() {
        int sizeBefore = cache.size();
        cache.clear();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Cache cleared successfully");
        response.put("entriesRemoved", sizeBefore);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear/{prefix}")
    public ResponseEntity<Map<String, Object>> clearCacheByPrefix(@PathVariable String prefix) {
        int sizeBefore = cache.size();
        cache.evictByPrefix(prefix + ":");
        int sizeAfter = cache.size();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Cache cleared for prefix: " + prefix);
        response.put("entriesRemoved", sizeBefore - sizeAfter);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/check/{key}")
    public ResponseEntity<Map<String, Object>> checkCache(@PathVariable String key) {
        boolean exists = cache.contains(key);

        Map<String, Object> response = new HashMap<>();
        response.put("key", key);
        response.put("cached", exists);

        if (exists) {
            response.put("message", "Key exists in cache");
        } else {
            response.put("message", "Key not found in cache");
        }

        return ResponseEntity.ok(response);
    }
}
