package com.vytsablinskas.flavorfare.business.services.interfaces;

import com.vytsablinskas.flavorfare.shared.dtos.table.AddTableDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.TableDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TableService {
    TableDto addTable(Integer restaurantId, AddTableDto addTableDto);

    List<TableDto> getTables(Integer restaurantId);
}
