package com.vytsablinskas.flavorfare.business.services.interfaces;

import com.vytsablinskas.flavorfare.shared.dtos.reservation.AddReservationDto;
import com.vytsablinskas.flavorfare.shared.dtos.reservation.ReservationDto;
import com.vytsablinskas.flavorfare.shared.dtos.reservation.UpdateReservationDto;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.RestaurantDto;

import java.util.List;

public interface ReservationService {
    List<ReservationDto> getRestaurantReservations(Integer restaurantId);

    ReservationDto addReservation(Integer restaurantId, Integer tableId, AddReservationDto addReservationDto);


    ReservationDto updateReservation(Integer restaurantId,
                                    Integer tableId,
                                    Integer reservationId,
                                    UpdateReservationDto updateReservationDto);

    void deleteReservation(Integer restaurantId, Integer tableId, Integer reservationId);
}