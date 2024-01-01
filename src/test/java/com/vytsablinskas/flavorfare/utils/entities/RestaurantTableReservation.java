package com.vytsablinskas.flavorfare.utils.entities;

import lombok.Builder;

@Builder
public record RestaurantTableReservation(Integer restaurantId, Integer tableId, Integer reservationId) { }