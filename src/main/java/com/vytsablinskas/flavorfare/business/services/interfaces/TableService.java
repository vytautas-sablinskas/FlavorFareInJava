package com.vytsablinskas.flavorfare.business.services.interfaces;

import com.vytsablinskas.flavorfare.shared.dtos.table.AddTableDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.TableDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.UpdateTableDto;

import java.util.List;

public interface TableService {
    List<TableDto> getTables(Integer restaurantId);

    TableDto getTable(Integer restaurantId, Integer tableId);

    TableDto addTable(Integer restaurantId, AddTableDto addTableDto);

    TableDto updateTable(Integer restaurantId, Integer tableId, UpdateTableDto updateTableDto);

    void deleteTable(Integer restaurantId, Integer tableId);
}