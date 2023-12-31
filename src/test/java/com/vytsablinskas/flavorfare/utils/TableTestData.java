package com.vytsablinskas.flavorfare.utils;

import com.vytsablinskas.flavorfare.database.domain.TableEntity;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.AddRestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.AddTableDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.TableDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.UpdateTableDto;

import java.util.Collections;
import java.util.List;

public class TableTestData {
    public static AddTableDto getAddTableDtoA() {
        return AddTableDto.builder()
                .size(5)
                .count(4)
                .build();
    }

    public static AddTableDto getAddTableDtoB() {
        return AddTableDto.builder()
                .size(3)
                .count(1)
                .build();
    }

    public static UpdateTableDto getUpdateTableDtoA() {
        return UpdateTableDto.builder()
                .size(3)
                .count(1)
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

    public static List<TableEntity> getTableEntityListA() {
        return Collections.singletonList(
                TableTestData.getTableEntityA()
        );
    }
}
