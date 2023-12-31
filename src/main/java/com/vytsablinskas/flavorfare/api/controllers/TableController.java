package com.vytsablinskas.flavorfare.api.controllers;

import com.vytsablinskas.flavorfare.business.services.interfaces.TableService;
import com.vytsablinskas.flavorfare.shared.constants.Prefixes;
import com.vytsablinskas.flavorfare.shared.dtos.table.AddTableDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.TableDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.UpdateTableDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = Prefixes.API_V1)
public class TableController {
    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping(path = "restaurants/{restaurantId}/tables")
    @ResponseStatus(HttpStatus.OK)
    public List<TableDto> getTables(@PathVariable Integer restaurantId) {
        return tableService.getTables(restaurantId);
    }

    @GetMapping(path = "restaurants/{restaurantId}/tables/{tableId}")
    @ResponseStatus(HttpStatus.OK)
    public TableDto getTable(@PathVariable Integer restaurantId, @PathVariable Integer tableId) {
        return tableService.getTable(restaurantId, tableId);
    }

    @PostMapping(path = "restaurants/{restaurantId}/tables")
    @ResponseStatus(HttpStatus.CREATED)
    public TableDto addTable(@PathVariable Integer restaurantId, @RequestBody AddTableDto addTableDto) {
        return tableService.addTable(restaurantId, addTableDto);
    }

    @PutMapping(path = "restaurants/{restaurantId}/tables/{tableId}")
    @ResponseStatus(HttpStatus.OK)
    public TableDto updateTable(@PathVariable Integer restaurantId, @PathVariable Integer tableId, @RequestBody UpdateTableDto updateTableDto) {
        return tableService.updateTable(restaurantId, tableId, updateTableDto);
    }

    @DeleteMapping(path = "restaurants/{restaurantId}/tables/{tableId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTable(@PathVariable Integer restaurantId, @PathVariable Integer tableId) {
        tableService.deleteTable(restaurantId, tableId);
    }
}