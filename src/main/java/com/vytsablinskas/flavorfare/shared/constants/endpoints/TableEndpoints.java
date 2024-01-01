package com.vytsablinskas.flavorfare.shared.constants.endpoints;

public class TableEndpoints {
    public static String tables(Integer restaurantId) {
        return String.format("/api/v1/restaurants/%d/tables", restaurantId);
    }

    public static String table(Integer restaurantId, Integer tableId) {
        return String.format("/api/v1/restaurants/%d/tables/%d", restaurantId, tableId);
    }

    public static String addTable(Integer restaurantId) {
        return String.format("/api/v1/restaurants/%d/tables", restaurantId);
    }

    public static String updateTable(Integer restaurantId, Integer tableId) {
        return String.format("/api/v1/restaurants/%d/tables/%d", restaurantId, tableId);
    }

    public static String deleteTable(Integer restaurantId, Integer tableId) {
        return String.format("/api/v1/restaurants/%d/tables/%d", restaurantId, tableId);
    }
}