package com.vytsablinskas.flavorfare.shared.dtos.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDto {
    private Integer id;

    private String name;

    private String address;

    private Duration openingTime;

    private Duration closingTime;

    private Duration intervalBetweenBookings;
}
