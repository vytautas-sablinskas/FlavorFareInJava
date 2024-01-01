package com.vytsablinskas.flavorfare.business.services.interfaces;

import com.vytsablinskas.flavorfare.shared.dtos.reservation.AddReservationDto;
import com.vytsablinskas.flavorfare.shared.dtos.reservation.ReservationDto;

import java.util.List;

public interface ReservationService {
    List<ReservationDto> getRestaurantReservations(Integer restaurantId);

    ReservationDto addReservation(Integer restaurantId, Integer tableId, AddReservationDto addReservationDto);
}