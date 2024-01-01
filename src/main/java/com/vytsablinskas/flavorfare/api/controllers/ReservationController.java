package com.vytsablinskas.flavorfare.api.controllers;

import com.vytsablinskas.flavorfare.shared.constants.Prefixes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(name = Prefixes.API_V1)
public class ReservationController {
    @GetMapping(path = "restaurants/{restaurantId}/reservations")
    @ResponseStatus(HttpStatus.OK)
    public void getRestaurantReservations(@PathVariable Integer restaurantId) {
    }

    @GetMapping(path = "restaurants/{restaurantId}/table/{tableId}/reservations/{reservationId}")
    @ResponseStatus(HttpStatus.OK)
    public void getReservationOfTable(@PathVariable Integer restaurantId, @PathVariable Integer tableId, @PathVariable Integer reservationId) {

    }

    @PostMapping(path = "restaurants/{restaurantId}/table/{tableId}/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public void addReservationToRestaurantTable(@PathVariable Integer restaurantId, @PathVariable Integer tableId) {

    }

    @PutMapping(path = "restaurants/{restaurantId}/table/{tableId}/reservations/{reservationId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateReservationToRestaurantTable(@PathVariable Integer restaurantId,
                                                   @PathVariable Integer tableId,
                                                   @PathVariable Integer reservationId) {

    }

    @DeleteMapping(path = "restaurants/{restaurantId}/table/{tableId}/reservations/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservationToRestaurantTable(@PathVariable Integer restaurantId, @PathVariable Integer tableId, @PathVariable Integer reservationId) {

    }
}
