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
public class ReservationDto {
    private Integer id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String extraInformation;

    private Integer tableId;
}