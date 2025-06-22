package com.example.demo.Services;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Central registry for per-user locks, shared across services.
 */
@Component
public class UserLockRegistry {
    private final ConcurrentHashMap<String, Object> locks = new ConcurrentHashMap<>();

    /**
     * Get (or create) the lock object for a given userId.
     */
    public Object getLock(String userId) {
        return locks.computeIfAbsent(userId, key -> new Object());
    }

    /**
     * Cleanup lock entry if it's not replaced (prevent unbounded growth).
     */
    public void cleanup(String userId, Object lock) {
        locks.compute(userId, (key, existing) -> existing == lock ? null : existing);
    }
}
