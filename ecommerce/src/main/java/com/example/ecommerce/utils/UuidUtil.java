package com.example.ecommerce.utils;

import java.util.UUID;

public class UuidUtil {
    public static UUID parseUUID(String id, String entityName) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid " + entityName + " ID format: " + id);
        }
    }
}
