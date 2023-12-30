package com.vytsablinskas.flavorfare.shared.dtos.table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTableDto {
    private Integer size;

    private Integer count;
}