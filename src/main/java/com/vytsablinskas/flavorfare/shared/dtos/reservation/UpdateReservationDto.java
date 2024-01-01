package com.vytsablinskas.flavorfare.shared.dtos.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UpdateReservationDto {
    private String extraInformation;
}