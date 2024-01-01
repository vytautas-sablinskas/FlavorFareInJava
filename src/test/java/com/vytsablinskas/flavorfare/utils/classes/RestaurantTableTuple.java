package com.vytsablinskas.flavorfare.utils.classes;

import lombok.Builder;

@Builder
public record RestaurantTableTuple(Integer restaurantId, Integer tableId) { }