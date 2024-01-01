package com.vytsablinskas.flavorfare.utils.entities;

import lombok.Builder;

@Builder
public record RestaurantTable(Integer restaurantId, Integer tableId) { }