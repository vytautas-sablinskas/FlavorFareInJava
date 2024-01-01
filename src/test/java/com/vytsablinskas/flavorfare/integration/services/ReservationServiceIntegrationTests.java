package com.vytsablinskas.flavorfare.integration.services;

import com.vytsablinskas.flavorfare.business.exceptions.ResourceNotFoundException;
import com.vytsablinskas.flavorfare.business.services.interfaces.ReservationService;
import com.vytsablinskas.flavorfare.business.services.interfaces.RestaurantService;
import com.vytsablinskas.flavorfare.business.services.interfaces.TableService;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationServiceIntegrationTests {
    private final ReservationService underTest;
    private final TableService tableService;
    private final RestaurantService restaurantService;

    @Autowired
    public ReservationServiceIntegrationTests(ReservationService reservationService, TableService tableService, RestaurantService restaurantService) {
        this.underTest = reservationService;
        this.tableService = tableService;
        this.restaurantService = restaurantService;
    }

    @Test
    public void getRestaurantReservations_validRestaurantId_shouldGetAllRestaurantReservations() {
        Integer restaurantId = addTwoTablesWithTwoReservationsToRestaurantAndGetRestaurantId();

        List<ReservationDto> restaurantReservations = underTest.getRestaurantReservations(restaurantId);

        assertThat(restaurantReservations).hasSize(2);
    }

    @Test
    public void getRestaurantReservations_invalidRestaurantId_shouldThrowResourceNotFoundException() {
        Integer invalidRestaurantId = 1;

        assertThatThrownBy(() ->
                underTest.getRestaurantReservations(invalidRestaurantId)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void addReservation_validIds_shouldAddReservationAndReturnCorrectReservation() {
        RestaurantTable restaurantTableInformation = addAndGetRestaurantTableInformation();
        AddReservationDto addReservationDtoA = ReservationTestData.getAddReservationDtoA();

        ReservationDto reservationDto = underTest.addReservation(restaurantTableInformation.restaurantId(),
                restaurantTableInformation.tableId(),
                addReservationDtoA);
        List<ReservationDto> restaurantReservations = underTest.getRestaurantReservations(restaurantTableInformation.restaurantId());

        assertThat(reservationDto.getStartTime()).isEqualTo(addReservationDtoA.getStartTime());
        assertThat(reservationDto.getEndTime()).isEqualTo(addReservationDtoA.getEndTime());
        assertThat(reservationDto.getExtraInformation()).isEqualTo(addReservationDtoA.getExtraInformation());
        assertThat(reservationDto.getTableId()).isEqualTo(restaurantTableInformation.tableId());
        assertThat(restaurantReservations).hasSize(1);
    }

    @Test
    public void addReservation_invalidRestaurantId_shouldThrowResourceNotFoundException() {
        Integer invalidRestaurantId = 1;
        Integer shouldNotReachTableId = 1;

        assertThatThrownBy(() ->
                    underTest.addReservation(invalidRestaurantId, shouldNotReachTableId, ReservationTestData.getAddReservationDtoA())
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void addReservation_invalidTableId_shouldThrowResourceNotFoundException() {
        Integer validRestaurantId = addAndGetRestaurantId();
        Integer invalidTableId = 1;

        assertThatThrownBy(() ->
                underTest.addReservation(validRestaurantId, invalidTableId, ReservationTestData.getAddReservationDtoA())
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void updateReservation_validIds_shouldUpdateReservationAndReturnIt() {
        RestaurantTableReservation information = addRestaurantWithTableAndReservation();
        UpdateReservationDto updateReservationDtoA = ReservationTestData.getUpdateReservationDtoA();

        ReservationDto reservationDto = underTest.updateReservation(information.restaurantId(),
                information.tableId(),
                information.reservationId(),
                updateReservationDtoA
                );
        ReservationDto reservationInDatabase = underTest.getRestaurantReservations(information.restaurantId())
                                                         .getFirst();


        assertThat(reservationDto).isEqualTo(reservationInDatabase);
        assertThat(reservationDto.getExtraInformation()).isEqualTo(updateReservationDtoA.getExtraInformation());
    }

    @Test
    public void updateReservation_invalidReservationId_shouldThrowResourceNotFoundException() {
        RestaurantTable restaurantTable = addAndGetRestaurantTableInformation();
        Integer invalidReservationId = 1;

        assertThatThrownBy(() ->
                underTest.updateReservation(restaurantTable.restaurantId(),
                        restaurantTable.tableId(),
                        invalidReservationId,
                        ReservationTestData.getUpdateReservationDtoA())
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void updateReservation_invalidTableId_shouldThrowResourceNotFoundException() {
        Integer restaurantId = addAndGetRestaurantId();
        Integer invalidTableId = 1;
        Integer shouldNotReachReservationId = 1;

        assertThatThrownBy(() ->
                underTest.updateReservation(restaurantId,
                        invalidTableId,
                        shouldNotReachReservationId,
                        ReservationTestData.getUpdateReservationDtoA())
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void updateReservation_invalidRestaurantId_shouldThrowResourceNotFoundException() {
        Integer invalidRestaurantId = 1;
        Integer shouldNotReachTableId = 1;
        Integer shouldNotReachReservationId = 1;

        assertThatThrownBy(() ->
                underTest.updateReservation(invalidRestaurantId,
                        shouldNotReachTableId,
                        shouldNotReachReservationId,
                        ReservationTestData.getUpdateReservationDtoA())
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void deleteReservation_validIds_shouldDeleteReservation() {
        RestaurantTableReservation information = addRestaurantWithTableAndReservation();

        underTest.deleteReservation(information.restaurantId(), information.tableId(), information.reservationId());
        List<ReservationDto> restaurantReservations = underTest.getRestaurantReservations(information.restaurantId());

        assertThat(restaurantReservations).hasSize(0);
    }

    @Test
    public void deleteReservation_invalidReservationId_shouldThrowResourceNotFoundException() {
        RestaurantTable restaurantTable = addAndGetRestaurantTableInformation();
        Integer invalidReservationId = 1;

        assertThatThrownBy(() ->
                underTest.deleteReservation(restaurantTable.restaurantId(),
                        restaurantTable.tableId(),
                        invalidReservationId)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void deleteReservation_invalidTableId_shouldThrowResourceNotFoundException() {
        Integer validRestaurantId = addAndGetRestaurantId();
        Integer invalidTableId = 1;
        Integer shouldNotReachReservationId = 1;

        assertThatThrownBy(() ->
                underTest.deleteReservation(validRestaurantId,
                        invalidTableId,
                        shouldNotReachReservationId)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void deleteReservation_invalidRestaurantId_shouldThrowResourceNotFoundException() {
        Integer invalidRestaurantId = 1;
        Integer shouldNotReachTableId = 1;
        Integer shouldNotReachReservationId = 1;

        assertThatThrownBy(() ->
                underTest.deleteReservation(invalidRestaurantId,
                        shouldNotReachTableId,
                        shouldNotReachReservationId)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    private RestaurantTable addAndGetRestaurantTableInformation() {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());
        TableDto table = tableService.addTable(restaurantDto.getId(), TableTestData.getAddTableDtoA());

        return RestaurantTable.builder()
                .restaurantId(restaurantDto.getId())
                .tableId(table.getId())
                .build();
    }

    private Integer addTwoTablesWithTwoReservationsToRestaurantAndGetRestaurantId() {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());
        TableDto firstTable = tableService.addTable(restaurantDto.getId(), TableTestData.getAddTableDtoA());
        TableDto secondTable = tableService.addTable(restaurantDto.getId(), TableTestData.getAddTableDtoB());
        underTest.addReservation(restaurantDto.getId(), firstTable.getId(), ReservationTestData.getAddReservationDtoA());
        underTest.addReservation(restaurantDto.getId(), secondTable.getId(), ReservationTestData.getAddReservationDtoB());

        return restaurantDto.getId();
    }

    private Integer addAndGetRestaurantId() {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());

        return restaurantDto.getId();
    }

    private RestaurantTableReservation addRestaurantWithTableAndReservation() {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());
        TableDto table = tableService.addTable(restaurantDto.getId(), TableTestData.getAddTableDtoA());
        ReservationDto reservationDto = underTest.addReservation(restaurantDto.getId(), table.getId(), ReservationTestData.getAddReservationDtoA());

        return RestaurantTableReservation.builder()
                .restaurantId(restaurantDto.getId())
                .tableId(table.getId())
                .reservationId(reservationDto.getId())
                .build();
    }
}
