package com.vytsablinskas.flavorfare.shared.dtos.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AddReservationDto {
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String extraInformation;
}
