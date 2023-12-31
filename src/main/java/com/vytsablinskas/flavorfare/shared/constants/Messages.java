package com.vytsablinskas.flavorfare.shared.constants;

public class Messages {
    public static String getRestaurantNotFoundMessage(Integer id) {
        return String.format("Restaurant by id '%d' was not found", id);
    }

    public static String getTableNotFoundMessage(Integer id) {
        return String.format("Table by id '%d' was not found", id);
    }

    public static String getTableOfSizeIsDuplicateMessage(Integer size) {
        return String.format("Table of size '%d' already exists", size);
    }
}
