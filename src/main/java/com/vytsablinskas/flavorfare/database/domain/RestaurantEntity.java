package com.vytsablinskas.flavorfare.database.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="restaurants")
public class RestaurantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer restaurantId;

    private String name;

    private String address;

    private Duration openingTime;

    private Duration closingTime;

    private Duration intervalBetweenBookings;
}