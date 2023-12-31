package com.vytsablinskas.flavorfare.utils;

import com.vytsablinskas.flavorfare.database.domain.TableEntity;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.AddRestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.AddTableDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.TableDto;

public class TableTestData {
    public static AddTableDto getAddTableDtoA() {
        return AddTableDto.builder()
                .size(5)
                .count(4)
                .build();
    }

    public static TableDto getTableDtoA() {
        return TableDto.builder()
                .count(1)
                .id(1)
                .restaurantId(1)
                .size(1)
                .build();
    }

    public static TableEntity getTableEntityA() {
        return TableEntity.builder()
                .tableId(1)
                .count(1)
                .size(1)
                .restaurant(RestaurantTestData.getRestaurantEntityA())
                .build();
    }
}
