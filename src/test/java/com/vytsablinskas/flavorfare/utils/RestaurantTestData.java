package com.vytsablinskas.flavorfare.utils;

import com.vytsablinskas.flavorfare.shared.dtos.restaurant.AddRestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.UpdateRestaurantDto;

import java.time.Duration;

public class RestaurantTestData {
    public static AddRestaurantDto getAddRestaurantDtoA() {
        return AddRestaurantDto.builder()
                                .name("fake name A")
                                .address("fake address A")
                                .openingTime(Duration.ofHours(8))
                                .closingTime(Duration.ofHours(20))
                                .intervalBetweenBookings(Duration.ofHours(3))
                                .build();
    }

    public static AddRestaurantDto getAddRestaurantDtoB() {
        return AddRestaurantDto.builder()
                .name("fake name B")
                .address("fake address B")
                .openingTime(Duration.ofHours(7))
                .closingTime(Duration.ofHours(21))
                .intervalBetweenBookings(Duration.ofHours(2))
                .build();
    }

    public static UpdateRestaurantDto getUpdateRestaurantDtoA() {
        return UpdateRestaurantDto.builder()
                .name("updated name A")
                .address("updated address A")
                .openingTime(Duration.ofHours(4))
                .closingTime(Duration.ofHours(8))
                .intervalBetweenBookings(Duration.ofHours(1))
                .build();
    }
}