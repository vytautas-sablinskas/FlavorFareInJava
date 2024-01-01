package com.vytsablinskas.flavorfare.api.controllers;

import com.vytsablinskas.flavorfare.business.services.interfaces.ReservationService;
import com.vytsablinskas.flavorfare.shared.constants.Prefixes;
import com.vytsablinskas.flavorfare.shared.dtos.reservation.AddReservationDto;
import com.vytsablinskas.flavorfare.shared.dtos.reservation.ReservationDto;
import com.vytsablinskas.flavorfare.shared.dtos.reservation.UpdateReservationDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = Prefixes.API_V1)
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping(path = "restaurants/{restaurantId}/reservations")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationDto> getRestaurantReservations(@PathVariable Integer restaurantId) {
        return reservationService.getRestaurantReservations(restaurantId);
    }

    @PostMapping(path = "restaurants/{restaurantId}/tables/{tableId}/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationDto addReservationToRestaurantTable(@PathVariable Integer restaurantId,
                                                          @PathVariable Integer tableId,
                                                          @RequestBody AddReservationDto addReservationDto) {
        return reservationService.addReservation(restaurantId, tableId, addReservationDto);
    }

    @PatchMapping(path = "restaurants/{restaurantId}/tables/{tableId}/reservations/{reservationId}")
    @ResponseStatus(HttpStatus.OK)
    public ReservationDto updateReservationToRestaurantTable(@PathVariable Integer restaurantId,
                                                            @PathVariable Integer tableId,
                                                            @PathVariable Integer reservationId,
                                                            @RequestBody UpdateReservationDto updateReservationDto) {
        return reservationService.updateReservation(restaurantId, tableId, reservationId, updateReservationDto);
    }

    @DeleteMapping(path = "restaurants/{restaurantId}/tables/{tableId}/reservations/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservationToRestaurantTable(@PathVariable Integer restaurantId, @PathVariable Integer tableId, @PathVariable Integer reservationId) {
        reservationService.deleteReservation(restaurantId, tableId, reservationId);
    }
}