package com.vytsablinskas.flavorfare.utils.data;

import com.vytsablinskas.flavorfare.shared.dtos.reservation.AddReservationDto;

import java.time.LocalDateTime;

public class ReservationTestData {
    public static AddReservationDto getAddReservationDtoA() {
        return AddReservationDto.builder()
                .startTime(LocalDateTime.of(2023, 10, 23, 10, 20))
                .endTime(LocalDateTime.of(2023, 10, 23, 12, 20))
                .extraInformation("Near window")
                .build();
    }

    public static AddReservationDto getAddReservationDtoB() {
        return AddReservationDto.builder()
                .startTime(LocalDateTime.of(2023, 11, 24, 10, 0))
                .endTime(LocalDateTime.of(2023, 11, 24, 11, 0))
                .extraInformation("Near window")
                .build();
    }
}
