package com.vytsablinskas.flavorfare.utils;

import com.vytsablinskas.flavorfare.database.domain.RestaurantEntity;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.AddRestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.RestaurantDto;
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

    public static RestaurantEntity getRestaurantEntityA() {
        return RestaurantEntity.builder()
                .id(4)
                .name("name entity")
                .address("address entity")
                .openingTime(Duration.ofHours(3))
                .closingTime(Duration.ofHours(15))
                .intervalBetweenBookings(Duration.ofHours(2))
                .build();
    }

    public static RestaurantDto getRestaurantDtoA() {
        return RestaurantDto.builder()
                .id(12)
                .name("actual restaurant dto name")
                .address("actual restaurant dto address")
                .openingTime(Duration.ofHours(1))
                .closingTime(Duration.ofHours(20))
                .intervalBetweenBookings(Duration.ofHours(1))
                .build();
    }
}