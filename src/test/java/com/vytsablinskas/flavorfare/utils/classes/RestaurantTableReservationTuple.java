package com.vytsablinskas.flavorfare.utils.classes;

import lombok.Builder;

@Builder
public record RestaurantTableReservationTuple(Integer restaurantId, Integer tableId, Integer reservationId) { }