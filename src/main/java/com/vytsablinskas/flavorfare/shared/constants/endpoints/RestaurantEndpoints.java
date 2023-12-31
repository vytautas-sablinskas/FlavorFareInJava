package com.vytsablinskas.flavorfare.shared.constants.endpoints;

public class RestaurantEndpoints {
    public static String restaurantsEndpoint = "/api/v1/restaurants";

    public static String addRestaurantEndpoint = "/api/v1/restaurants";

    public static String restaurantEndpoint(Integer restaurantId) {
        return String.format("/api/v1/restaurants/%d", restaurantId);
    }

    public static String updateRestaurantEndpoint(Integer restaurantId) {
        return String.format("/api/v1/restaurants/%d", restaurantId);
    }

    public static String deleteRestaurantEndpoint(Integer restaurantId) {
        return String.format("/api/v1/restaurants/%d", restaurantId);
    }
}
