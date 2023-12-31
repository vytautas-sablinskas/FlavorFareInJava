package com.vytsablinskas.flavorfare.api.controllers;

import com.vytsablinskas.flavorfare.business.services.interfaces.TableService;
import com.vytsablinskas.flavorfare.shared.constants.Prefixes;
import com.vytsablinskas.flavorfare.shared.dtos.table.AddTableDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.TableDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = Prefixes.API_V1)
public class TableController {
    private TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping(path = "restaurants/{restaurantId}/tables")
    @ResponseStatus(HttpStatus.OK)
    public List<TableDto> getTables(@PathVariable Integer restaurantId) {
        return null;
    }

    @PostMapping(path = "restaurants/{restaurantId}/tables")
    public TableDto addTable(@PathVariable Integer restaurantId, @RequestBody AddTableDto addTableDto) {
        return tableService.addTable(restaurantId, addTableDto);
    }
}
