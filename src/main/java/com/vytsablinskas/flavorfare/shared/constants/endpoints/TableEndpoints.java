package com.vytsablinskas.flavorfare.shared.constants.endpoints;

public class TableEndpoints {
    public static String tablesEndpoint(Integer restaurantId) {
        return String.format("/api/v1/restaurants/%d/tables", restaurantId);
    }

    public static String tableEndpoint(Integer restaurantId, Integer tableId) {
        return String.format("/api/v1/restaurants/%d/tables/%d", restaurantId, tableId);
    }

    public static String addTableEndpoint(Integer restaurantId) {
        return String.format("/api/v1/restaurants/%d/tables", restaurantId);
    }

    public static String updateTableEndpoint(Integer restaurantId, Integer tableId) {
        return String.format("/api/v1/restaurants/%d/tables/%d", restaurantId, tableId);
    }

    public static String deleteTableEndpoint(Integer restaurantId, Integer tableId) {
        return String.format("/api/v1/restaurants/%d/tables/%d", restaurantId, tableId);
    }
}