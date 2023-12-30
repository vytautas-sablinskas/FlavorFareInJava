package com.vytsablinskas.flavorfare.shared.dtos.table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableDto {
    private Integer id;

    private Integer size;

    private Integer count;

    private Integer restaurantId;
}