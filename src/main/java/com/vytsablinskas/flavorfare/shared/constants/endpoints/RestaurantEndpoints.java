package com.vytsablinskas.flavorfare.shared.constants.endpoints;

public class RestaurantEndpoints {
    public static String restaurants = "/api/v1/restaurants";

    public static String addRestaurant = "/api/v1/restaurants";

    public static String restaurant(Integer restaurantId) {
        return String.format("/api/v1/restaurants/%d", restaurantId);
    }

    public static String updateRestaurant(Integer restaurantId) {
        return String.format("/api/v1/restaurants/%d", restaurantId);
    }

    public static String deleteRestaurant(Integer restaurantId) {
        return String.format("/api/v1/restaurants/%d", restaurantId);
    }
}
