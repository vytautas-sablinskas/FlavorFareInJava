package com.vytsablinskas.flavorfare.integration.services;

import com.vytsablinskas.flavorfare.business.exceptions.ResourceNotFoundException;
import com.vytsablinskas.flavorfare.business.services.interfaces.ReservationService;
import com.vytsablinskas.flavorfare.business.services.interfaces.RestaurantService;
import com.vytsablinskas.flavorfare.business.services.interfaces.TableService;
import com.vytsablinskas.flavorfare.shared.dtos.reservation.AddReservationDto;
import com.vytsablinskas.flavorfare.shared.dtos.reservation.ReservationDto;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.RestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.TableDto;
import com.vytsablinskas.flavorfare.utils.classes.RestaurantTableTuple;
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
        RestaurantTableTuple restaurantTableInformation = addAndGetRestaurantTableInformation();
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
        Integer validRestaurantId = addAndGetRestaurantInformation();
        Integer invalidTableId = 1;

        assertThatThrownBy(() ->
                underTest.addReservation(validRestaurantId, invalidTableId, ReservationTestData.getAddReservationDtoA())
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    private RestaurantTableTuple addAndGetRestaurantTableInformation() {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());
        TableDto table = tableService.addTable(restaurantDto.getId(), TableTestData.getAddTableDtoA());

        return RestaurantTableTuple.builder()
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

    private Integer addAndGetRestaurantInformation() {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());

        return restaurantDto.getId();
    }
}
