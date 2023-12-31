package com.vytsablinskas.flavorfare.business.services.interfaces;

import com.vytsablinskas.flavorfare.shared.dtos.table.AddTableDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.TableDto;

import java.util.List;

public interface TableService {
    List<TableDto> getTables(Integer restaurantId);

    TableDto getTable(Integer restaurantId, Integer tableId);

    TableDto addTable(Integer restaurantId, AddTableDto addTableDto);
}
