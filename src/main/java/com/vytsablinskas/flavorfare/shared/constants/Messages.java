package com.vytsablinskas.flavorfare.shared.constants;

public class Messages {
    public static String GetRestaurantNotFoundMessage(Integer id) {
        return String.format("Restaurant by id '%d' was not found", id);
    }
}
