package com.vytsablinskas.flavorfare.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vytsablinskas.flavorfare.business.services.interfaces.RestaurantService;
import com.vytsablinskas.flavorfare.business.services.interfaces.TableService;
import com.vytsablinskas.flavorfare.shared.constants.Messages;
import com.vytsablinskas.flavorfare.shared.constants.endpoints.TableEndpoints;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.RestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.AddTableDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.TableDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.UpdateTableDto;
import com.vytsablinskas.flavorfare.utils.data.RestaurantTestData;
import com.vytsablinskas.flavorfare.utils.data.TableTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class TableControllerIntegrationTests {
    private final MockMvc mockMvc;
    private final TableService tableService;
    private final RestaurantService restaurantService;
    private final ObjectMapper objectMapper;

    @Autowired
    public TableControllerIntegrationTests(MockMvc mockMvc, TableService tableService, RestaurantService restaurantService, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.tableService = tableService;
        this.restaurantService = restaurantService;
        this.objectMapper = objectMapper;
    }

    @Test
    public void getTables_validRestaurantId_returnsHttpStatus200() throws Exception {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());

        mockMvc.perform(
                MockMvcRequestBuilders.get(TableEndpoints.tablesEndpoint(restaurantDto.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void getTables_invalidRestaurantId_returnsHttpStatus404WithMessage() throws Exception {
        Integer invalidRestaurantId = 1;

        mockMvc.perform(
                MockMvcRequestBuilders.get(TableEndpoints.tablesEndpoint(invalidRestaurantId))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.content().string(Messages.getRestaurantNotFoundMessage(invalidRestaurantId))
        );
    }

    @Test
    public void getTables_validRestaurantId_shouldReturnTableList() throws Exception {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());
        TableDto table = tableService.addTable(restaurantDto.getId(), TableTestData.getAddTableDtoA());

        mockMvc.perform(
                MockMvcRequestBuilders.get(TableEndpoints.tablesEndpoint(restaurantDto.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").value(table.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].size").value(table.getSize())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].count").value(table.getCount())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].restaurantId").value(restaurantDto.getId())
        );
    }

    @Test
    public void getTable_validIds_returnsHttpStatus200() throws Exception {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());
        TableDto table = tableService.addTable(restaurantDto.getId(), TableTestData.getAddTableDtoA());

        mockMvc.perform(
                MockMvcRequestBuilders.get(TableEndpoints.tableEndpoint(restaurantDto.getId(), table.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void getTable_invalidRestaurantId_returnsHttpStatus404WithMessage() throws Exception {
        Integer invalidRestaurantId = 1;
        Integer shouldNotReachTableId = 1;

        mockMvc.perform(
                MockMvcRequestBuilders.get(TableEndpoints.tableEndpoint(invalidRestaurantId, shouldNotReachTableId))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.content().string(Messages.getRestaurantNotFoundMessage(invalidRestaurantId))
        );
    }

    @Test
    public void getTable_invalidTableId_returnsHttpStatus404WithMessage() throws Exception {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());
        Integer invalidTableId = 1;

        mockMvc.perform(
                MockMvcRequestBuilders.get(TableEndpoints.tableEndpoint(restaurantDto.getId(), invalidTableId))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.content().string(Messages.getTableNotFoundMessage(invalidTableId))
        );
    }

    @Test
    public void getTable_validRestaurantAndTableId_shouldReturnTableInformation() throws Exception {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());
        TableDto table = tableService.addTable(restaurantDto.getId(), TableTestData.getAddTableDtoA());

        mockMvc.perform(
                MockMvcRequestBuilders.get(TableEndpoints.tableEndpoint(restaurantDto.getId(), table.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(table.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.size").value(table.getSize())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.count").value(table.getCount())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.restaurantId").value(restaurantDto.getId())
        );
    }

    @Test
    public void addTable_validRestaurantId_returnsHttpStatus201() throws Exception {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());
        AddTableDto addTableDtoA = TableTestData.getAddTableDtoA();
        String addTableDtoJson = objectMapper.writeValueAsString(addTableDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post(TableEndpoints.addTableEndpoint(restaurantDto.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addTableDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void addTable_invalidRestaurantId_returnsHttpStatus404WithMessage() throws Exception {
        Integer invalidRestaurantId = 1;
        AddTableDto addTableDtoA = TableTestData.getAddTableDtoA();
        String addTableDtoJson = objectMapper.writeValueAsString(addTableDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post(TableEndpoints.addTableEndpoint(invalidRestaurantId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addTableDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.content().string(Messages.getRestaurantNotFoundMessage(invalidRestaurantId))
        );
    }

    @Test
    public void addTable_tableSizeAlreadyInDatabase_returnsHttpStatus422WithMessage() throws Exception {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());
        AddTableDto addTableDtoA = TableTestData.getAddTableDtoA();
        tableService.addTable(restaurantDto.getId(), addTableDtoA);
        AddTableDto addTableDtoB = TableTestData.getAddTableDtoB();
        addTableDtoB.setSize(addTableDtoA.getSize());
        String addTableDtoJson = objectMapper.writeValueAsString(addTableDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.post(TableEndpoints.addTableEndpoint(restaurantDto.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addTableDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isUnprocessableEntity()
        ).andExpect(
                MockMvcResultMatchers.content().string(Messages.getTableOfSizeIsDuplicateMessage(addTableDtoA.getSize()))
        );
    }

    @Test
    public void addTable_validRestaurantId_shouldAddTableAndReturnItsInformation() throws Exception {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());
        AddTableDto addTableDtoA = TableTestData.getAddTableDtoA();
        String addTableDtoJson = objectMapper.writeValueAsString(addTableDtoA);


        mockMvc.perform(
                MockMvcRequestBuilders.post(TableEndpoints.addTableEndpoint(restaurantDto.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addTableDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.size").value(addTableDtoA.getSize())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.count").value(addTableDtoA.getCount())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.restaurantId").value(restaurantDto.getId())
        );
    }

    @Test
    public void updateTable_validIds_returnsHttpStatus200() throws Exception {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());
        TableDto table = tableService.addTable(restaurantDto.getId(), TableTestData.getAddTableDtoA());
        UpdateTableDto updateTableDtoA = TableTestData.getUpdateTableDtoA();
        String addTableDtoJson = objectMapper.writeValueAsString(updateTableDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put(TableEndpoints.updateTableEndpoint(restaurantDto.getId(), table.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addTableDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void updateTable_invalidRestaurantId_returnsHttpStatus404WithMessage() throws Exception {
        Integer invalidRestaurantId = 1;
        Integer shouldNotReachTableId = 1;
        UpdateTableDto updateTableDto = TableTestData.getUpdateTableDtoA();
        String updateTableDtoJson = objectMapper.writeValueAsString(updateTableDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put(TableEndpoints.updateTableEndpoint(invalidRestaurantId, shouldNotReachTableId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateTableDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.content().string(Messages.getRestaurantNotFoundMessage(invalidRestaurantId))
        );
    }

    @Test
    public void updateTable_invalidTableId_returnsHttpStatus404WithMessage() throws Exception {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());
        Integer invalidTableId = 1;
        String updateTableDtoJson = objectMapper.writeValueAsString(TableTestData.getUpdateTableDtoA());

        mockMvc.perform(
                MockMvcRequestBuilders.put(TableEndpoints.updateTableEndpoint(restaurantDto.getId(), invalidTableId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateTableDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.content().string(Messages.getTableNotFoundMessage(invalidTableId))
        );
    }

    @Test
    public void updateTable_tableSizeAlreadyInDatabase_returnsHttpStatus422WithMessage() throws Exception {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());
        TableDto table = tableService.addTable(restaurantDto.getId(), TableTestData.getAddTableDtoA());
        TableDto table2 = tableService.addTable(restaurantDto.getId(), TableTestData.getAddTableDtoB());
        UpdateTableDto updateTableDtoA = TableTestData.getUpdateTableDtoA();
        updateTableDtoA.setSize(table.getSize());
        String updateTableDtoJson = objectMapper.writeValueAsString(updateTableDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put(TableEndpoints.updateTableEndpoint(restaurantDto.getId(), table2.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateTableDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isUnprocessableEntity()
        ).andExpect(
                MockMvcResultMatchers.content().string(Messages.getTableOfSizeIsDuplicateMessage(updateTableDtoA.getSize()))
        );
    }

    @Test
    public void updateTable_validIds_shouldUpdateTableAndReturnItsInformation() throws Exception {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());
        TableDto table = tableService.addTable(restaurantDto.getId(), TableTestData.getAddTableDtoA());
        UpdateTableDto updateTableDtoA = TableTestData.getUpdateTableDtoA();
        String addTableDtoJson = objectMapper.writeValueAsString(updateTableDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put(TableEndpoints.updateTableEndpoint(restaurantDto.getId(), table.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addTableDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(table.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.size").value(updateTableDtoA.getSize())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.count").value(updateTableDtoA.getCount())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.restaurantId").value(restaurantDto.getId())
        );
    }

    @Test
    public void deleteTable_validIds_returnsHttpStatus200() throws Exception {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());
        TableDto table = tableService.addTable(restaurantDto.getId(), TableTestData.getAddTableDtoA());

        mockMvc.perform(
                MockMvcRequestBuilders.delete(TableEndpoints.deleteTableEndpoint(restaurantDto.getId(), table.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void deleteTable_invalidRestaurantId_returnsHttpStatus404WithMessage() throws Exception {
        Integer invalidRestaurantId = 1;
        Integer shouldNotReachTableId = 1;

        mockMvc.perform(
                MockMvcRequestBuilders.delete(TableEndpoints.deleteTableEndpoint(invalidRestaurantId, shouldNotReachTableId))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.content().string(Messages.getRestaurantNotFoundMessage(invalidRestaurantId))
        );
    }

    @Test
    public void deleteTable_invalidTableId_returnsHttpStatus404WithMessage() throws Exception {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());
        Integer invalidTableId = 1;

        mockMvc.perform(
                MockMvcRequestBuilders.delete(TableEndpoints.deleteTableEndpoint(restaurantDto.getId(), invalidTableId))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.content().string(Messages.getTableNotFoundMessage(invalidTableId))
        );
    }
}
