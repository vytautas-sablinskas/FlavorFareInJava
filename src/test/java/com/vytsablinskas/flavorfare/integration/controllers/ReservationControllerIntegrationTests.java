package com.vytsablinskas.flavorfare.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vytsablinskas.flavorfare.business.services.interfaces.ReservationService;
import com.vytsablinskas.flavorfare.business.services.interfaces.RestaurantService;
import com.vytsablinskas.flavorfare.business.services.interfaces.TableService;
import com.vytsablinskas.flavorfare.shared.constants.Messages;
import com.vytsablinskas.flavorfare.shared.constants.endpoints.ReservationEndpoints;
import com.vytsablinskas.flavorfare.shared.dtos.reservation.AddReservationDto;
import com.vytsablinskas.flavorfare.shared.dtos.reservation.ReservationDto;
import com.vytsablinskas.flavorfare.shared.dtos.reservation.UpdateReservationDto;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.RestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.TableDto;
import com.vytsablinskas.flavorfare.utils.entities.RestaurantTableReservation;
import com.vytsablinskas.flavorfare.utils.entities.RestaurantTable;
import com.vytsablinskas.flavorfare.utils.data.ReservationTestData;
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

import java.time.format.DateTimeFormatter;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ReservationControllerIntegrationTests {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final RestaurantService restaurantService;
    private final TableService tableService;
    private final ReservationService reservationService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Autowired
    public ReservationControllerIntegrationTests(MockMvc mockMvc,
                                                 ObjectMapper objectMapper,
                                                 RestaurantService restaurantService,
                                                 TableService tableService,
                                                 ReservationService reservationService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.restaurantService = restaurantService;
        this.tableService = tableService;
        this.reservationService = reservationService;
    }

    @Test
    public void getRestaurantReservations_validRestaurantId_shouldReturnHttpStatus200() throws Exception {
        RestaurantTableReservation restaurantTableReservationTuple = addRestaurantWithTableAndReservation();

        mockMvc.perform(
                MockMvcRequestBuilders.get(ReservationEndpoints.restaurantReservations(restaurantTableReservationTuple.restaurantId()))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void getRestaurantReservations_invalidRestaurantId_shouldReturnHttpStatus404WithMessage() throws Exception {
        Integer invalidRestaurantId = 1;

        mockMvc.perform(
                MockMvcRequestBuilders.get(ReservationEndpoints.restaurantReservations(invalidRestaurantId))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.content().string(Messages.getRestaurantNotFoundMessage(invalidRestaurantId))
        );
    }

    @Test
    public void getRestaurantReservations_validRestaurantId_shouldReturnCorrectInformationToUser() throws Exception {
        RestaurantTableReservation restaurantTableReservationTuple = addRestaurantWithTableAndReservation();
        AddReservationDto expectedReservationInfo = ReservationTestData.getAddReservationDtoA();

        mockMvc.perform(
                MockMvcRequestBuilders.get(ReservationEndpoints.restaurantReservations(restaurantTableReservationTuple.restaurantId()))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").value(restaurantTableReservationTuple.reservationId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].startTime").value(formatter.format(expectedReservationInfo.getStartTime()))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].endTime").value(formatter.format(expectedReservationInfo.getEndTime()))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].extraInformation").value(expectedReservationInfo.getExtraInformation())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].tableId").value(restaurantTableReservationTuple.tableId())
        );
    }

    @Test
    public void addReservation_validIds_shouldReturnHttpStatus201() throws Exception {
        RestaurantTable restaurantTable = addRestaurantWithTable();
        AddReservationDto addReservationDtoA = ReservationTestData.getAddReservationDtoA();
        String reservationToAddJson = objectMapper.writeValueAsString(addReservationDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post(ReservationEndpoints.addReservation(restaurantTable.restaurantId(),
                                                                                restaurantTable.tableId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationToAddJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void addReservation_invalidRestaurantId_shouldReturnHttpStatus404WithMessage() throws Exception {
        Integer invalidRestaurantId = 1;
        Integer shouldNotReachTableId = 1;
        AddReservationDto addReservationDtoA = ReservationTestData.getAddReservationDtoA();
        String reservationToAddJson = objectMapper.writeValueAsString(addReservationDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post(ReservationEndpoints.addReservation(invalidRestaurantId, shouldNotReachTableId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationToAddJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.content().string(Messages.getRestaurantNotFoundMessage(invalidRestaurantId))
        );
    }

    @Test
    public void addReservation_invalidTableId_shouldReturnHttpStatus404WithMessage() throws Exception {
        Integer validRestaurantId = addRestaurant();
        Integer invalidTableId = 1;
        AddReservationDto addReservationDtoA = ReservationTestData.getAddReservationDtoA();
        String reservationToAddJson = objectMapper.writeValueAsString(addReservationDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post(ReservationEndpoints.addReservation(validRestaurantId, invalidTableId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationToAddJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.content().string(Messages.getTableNotFoundMessage(invalidTableId))
        );
    }

    @Test
    public void addReservation_validIds_shouldReturnCorrectAddedDtoInformation() throws Exception {
        RestaurantTable restaurantTable = addRestaurantWithTable();
        AddReservationDto addReservationDtoA = ReservationTestData.getAddReservationDtoA();
        String reservationToAddJson = objectMapper.writeValueAsString(addReservationDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post(ReservationEndpoints.addReservation(restaurantTable.restaurantId(),
                                restaurantTable.tableId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationToAddJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.startTime").value(formatter.format(addReservationDtoA.getStartTime()))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.endTime").value(formatter.format(addReservationDtoA.getEndTime()))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.extraInformation").value(addReservationDtoA.getExtraInformation())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.tableId").value(restaurantTable.tableId())
        );
    }

    @Test
    public void updateReservation_validIds_shouldReturnHttpStatus200() throws Exception {
        RestaurantTableReservation restaurantTableReservation = addRestaurantWithTableAndReservation();
        UpdateReservationDto updateReservationDtoA = ReservationTestData.getUpdateReservationDtoA();
        String updateReservationJson = objectMapper.writeValueAsString(updateReservationDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch(ReservationEndpoints.updateReservation(restaurantTableReservation.restaurantId(),
                                restaurantTableReservation.tableId(),
                                restaurantTableReservation.reservationId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateReservationJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void updateReservation_invalidReservationId_shouldReturnHttpStatus404WithMessage() throws Exception {
        Integer invalidReservationId = 1;
        RestaurantTable restaurantTable = addRestaurantWithTable();
        UpdateReservationDto updateReservationDtoA = ReservationTestData.getUpdateReservationDtoA();
        String updateReservationJson = objectMapper.writeValueAsString(updateReservationDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch(ReservationEndpoints.updateReservation(restaurantTable.restaurantId(),
                                restaurantTable.tableId(),
                                invalidReservationId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateReservationJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.content().string(Messages.getReservationNotFoundMessage(invalidReservationId))
        );
    }

    @Test
    public void updateReservation_invalidTableId_shouldReturnHttpStatus404WithMessage() throws Exception {
        Integer validRestaurantId = addRestaurant();
        Integer invalidTableId = 1;
        Integer shouldNotReachReservationId = 1;
        UpdateReservationDto updateReservationDtoA = ReservationTestData.getUpdateReservationDtoA();
        String updateReservationJson = objectMapper.writeValueAsString(updateReservationDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch(ReservationEndpoints.updateReservation(validRestaurantId,
                                invalidTableId,
                                shouldNotReachReservationId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateReservationJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.content().string(Messages.getTableNotFoundMessage(invalidTableId))
        );
    }

    @Test
    public void updateReservation_invalidRestaurantId_shouldReturnHttpStatus404WithMessage() throws Exception {
        Integer invalidRestaurantId = 1;
        Integer shouldNotReachTableId = 1;
        Integer shouldNotReachReservationId = 1;
        UpdateReservationDto updateReservationDtoA = ReservationTestData.getUpdateReservationDtoA();
        String updateReservationJson = objectMapper.writeValueAsString(updateReservationDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch(ReservationEndpoints.updateReservation(invalidRestaurantId,
                                shouldNotReachTableId,
                                shouldNotReachReservationId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateReservationJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.content().string(Messages.getRestaurantNotFoundMessage(invalidRestaurantId))
        );
    }

    @Test
    public void updateReservation_validIds_shouldReturnUpdatedReservationInformation() throws Exception {
        RestaurantTableReservation restaurantTableReservation = addRestaurantWithTableAndReservation();
        UpdateReservationDto updateReservationDtoA = ReservationTestData.getUpdateReservationDtoA();
        AddReservationDto notUpdatedInformation = ReservationTestData.getAddReservationDtoA();
        String updateReservationJson = objectMapper.writeValueAsString(updateReservationDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch(ReservationEndpoints.updateReservation(restaurantTableReservation.restaurantId(),
                                restaurantTableReservation.tableId(),
                                restaurantTableReservation.reservationId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateReservationJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.startTime").value(formatter.format(notUpdatedInformation.getStartTime()))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.endTime").value(formatter.format(notUpdatedInformation.getEndTime()))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.extraInformation").value(updateReservationDtoA.getExtraInformation())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.tableId").value(restaurantTableReservation.tableId())
        );
    }

    @Test
    public void deleteReservation_validIds_shouldReturnHttpStatus204() throws Exception {
        RestaurantTableReservation restaurantTableReservation = addRestaurantWithTableAndReservation();

        mockMvc.perform(
                MockMvcRequestBuilders.delete(ReservationEndpoints.deleteReservation(restaurantTableReservation.restaurantId(),
                                restaurantTableReservation.tableId(),
                                restaurantTableReservation.reservationId()))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void deleteReservation_invalidReservationId_shouldReturnHttpStatus404WithMessage() throws Exception {
        Integer invalidReservationId = 1;
        RestaurantTable restaurantTable = addRestaurantWithTable();

        mockMvc.perform(
                MockMvcRequestBuilders.delete(ReservationEndpoints.deleteReservation(restaurantTable.restaurantId(),
                                restaurantTable.tableId(),
                                invalidReservationId))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.content().string(Messages.getReservationNotFoundMessage(invalidReservationId))
        );
    }

    @Test
    public void deleteReservation_invalidTableId_shouldReturnHttpStatus404WithMessage() throws Exception {
        Integer validRestaurantId = addRestaurant();
        Integer invalidTableId = 1;
        Integer shouldNotReachReservationId = 1;

        mockMvc.perform(
                MockMvcRequestBuilders.delete(ReservationEndpoints.updateReservation(validRestaurantId,
                                invalidTableId,
                                shouldNotReachReservationId))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.content().string(Messages.getTableNotFoundMessage(invalidTableId))
        );
    }

    @Test
    public void deleteReservation_invalidRestaurantId_shouldReturnHttpStatus404WithMessage() throws Exception {
        Integer invalidRestaurantId = 1;
        Integer shouldNotReachTableId = 1;
        Integer shouldNotReachReservationId = 1;

        mockMvc.perform(
                MockMvcRequestBuilders.delete(ReservationEndpoints.updateReservation(invalidRestaurantId,
                                shouldNotReachTableId,
                                shouldNotReachReservationId))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.content().string(Messages.getRestaurantNotFoundMessage(invalidRestaurantId))
        );
    }

    private RestaurantTableReservation addRestaurantWithTableAndReservation() {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());
        TableDto table = tableService.addTable(restaurantDto.getId(), TableTestData.getAddTableDtoA());
        ReservationDto reservationDto = reservationService.addReservation(restaurantDto.getId(),
                table.getId(),
                ReservationTestData.getAddReservationDtoA());

        return RestaurantTableReservation.builder()
                .restaurantId(restaurantDto.getId())
                .tableId(table.getId())
                .reservationId(reservationDto.getId())
                .build();
    }

    private RestaurantTable addRestaurantWithTable() {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());
        TableDto table = tableService.addTable(restaurantDto.getId(), TableTestData.getAddTableDtoA());

        return RestaurantTable.builder()
                .restaurantId(restaurantDto.getId())
                .tableId(table.getId())
                .build();
    }

    private Integer addRestaurant() {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());

        return restaurantDto.getId();
    }
}
