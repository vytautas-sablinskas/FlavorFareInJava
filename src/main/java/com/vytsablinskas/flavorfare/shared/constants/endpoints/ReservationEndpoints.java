package com.vytsablinskas.flavorfare.shared.constants.endpoints;

public class ReservationEndpoints {
    public static String restaurantReservations(Integer restaurantId) {
        return String.format("/api/v1/restaurants/%d/reservations", restaurantId);
    }

    public static String addReservation(Integer restaurantId, Integer tableId) {
        return String.format("/api/v1/restaurants/%d/tables/%d/reservations", restaurantId, tableId);
    }

    public static String updateReservation(Integer restaurantId, Integer tableId, Integer reservationId) {
        return String.format("/api/v1/restaurants/%d/tables/%d/reservations/%d", restaurantId, tableId, reservationId);
    }

    public static String deleteReservation(Integer restaurantId, Integer tableId, Integer reservationId) {
        return String.format("/api/v1/restaurants/%d/tables/%d/reservations/%d", restaurantId, tableId, reservationId);
    }
}
