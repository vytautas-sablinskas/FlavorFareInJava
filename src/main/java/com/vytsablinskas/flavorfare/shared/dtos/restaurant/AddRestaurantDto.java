package com.vytsablinskas.flavorfare.shared.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddRestaurantDto {
    private String name;

    private String address;

    private Duration openingTime;

    private Duration closingTime;

    private Duration intervalBetweenBookings;
}