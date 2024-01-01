package com.vytsablinskas.flavorfare.unit.services;

import com.vytsablinskas.flavorfare.business.exceptions.ResourceNotFoundException;
import com.vytsablinskas.flavorfare.business.services.impl.ReservationServiceImpl;
import com.vytsablinskas.flavorfare.database.domain.ReservationEntity;
import com.vytsablinskas.flavorfare.database.repositories.ReservationRepository;
import com.vytsablinskas.flavorfare.database.repositories.RestaurantRepository;
import com.vytsablinskas.flavorfare.database.repositories.TableRepository;
import com.vytsablinskas.flavorfare.shared.dtos.reservation.AddReservationDto;
import com.vytsablinskas.flavorfare.shared.dtos.reservation.ReservationDto;
import com.vytsablinskas.flavorfare.shared.dtos.reservation.UpdateReservationDto;
import com.vytsablinskas.flavorfare.utils.data.ReservationTestData;
import com.vytsablinskas.flavorfare.utils.data.RestaurantTestData;
import com.vytsablinskas.flavorfare.utils.data.TableTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceUnitTests {
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private TableRepository tableRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private ReservationServiceImpl underTest;

    @Test
    public void getRestaurantReservations_validRestaurantId_shouldReturnListOfRestaurantReservations() {
        Integer validRestaurantId = 1;
        ReservationDto expectedResult = ReservationTestData.getReservationDtoA();

        when(restaurantRepository.findById(validRestaurantId))
                .thenReturn(Optional.of(RestaurantTestData.getRestaurantEntityA()));
        when(reservationRepository.findByRestaurantId(any(Integer.class)))
                .thenReturn(Collections.singletonList(
                        ReservationTestData.getReservationEntityA()
                ));
        when(modelMapper.map(any(ReservationEntity.class), eq(ReservationDto.class)))
                .thenReturn(expectedResult);

        List<ReservationDto> restaurantReservations = underTest.getRestaurantReservations(validRestaurantId);

        assertThat(restaurantReservations).hasSize(1);
        assertThat(restaurantReservations.getFirst()).isEqualTo(expectedResult);
    }

    @Test
    public void getRestaurantReservations_invalidRestaurantId_shouldThrowResourceNotFoundException() {
        Integer invalidRestaurantId = 1;

        when(restaurantRepository.findById(invalidRestaurantId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                underTest.getRestaurantReservations(invalidRestaurantId)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void addReservation_validIds_shouldReturnAddedReservationDto() {
        Integer validRestaurantId = 1;
        Integer validTableId = 1;
        ReservationDto expectedResult = ReservationTestData.getReservationDtoA();
        ReservationEntity reservationEntityA = ReservationTestData.getReservationEntityA();

        when(restaurantRepository.findById(validRestaurantId))
                .thenReturn(Optional.of(RestaurantTestData.getRestaurantEntityA()));
        when(tableRepository.findById(validTableId))
                .thenReturn(Optional.of(TableTestData.getTableEntityA()));
        when(modelMapper.map(any(AddReservationDto.class), eq(ReservationEntity.class)))
                .thenReturn(reservationEntityA);
        when(reservationRepository.save(any(ReservationEntity.class)))
                .thenReturn(reservationEntityA);
        when(modelMapper.map(any(ReservationEntity.class), eq(ReservationDto.class)))
                .thenReturn(expectedResult);

        ReservationDto result = underTest.addReservation(validRestaurantId, validTableId, ReservationTestData.getAddReservationDtoA());

        assertThat(expectedResult).isEqualTo(result);
    }

    @Test
    public void addReservation_invalidTableId_shouldThrowResourceNotFoundException() {
        Integer validRestaurantId = 1;
        Integer invalidTableId = 1;

        when(restaurantRepository.findById(validRestaurantId))
                .thenReturn(Optional.of(RestaurantTestData.getRestaurantEntityA()));
        when(tableRepository.findById(invalidTableId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            underTest.addReservation(validRestaurantId, invalidTableId, ReservationTestData.getAddReservationDtoA())
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void addReservation_invalidRestaurantId_shouldThrowResourceNotFoundException() {
        Integer invalidRestaurantId = 1;
        Integer shouldNotReachTableId = 1;

        when(restaurantRepository.findById(invalidRestaurantId))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() ->
                underTest.addReservation(invalidRestaurantId, shouldNotReachTableId, ReservationTestData.getAddReservationDtoA())
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void updateReservation_validIds_shouldReturnUpdatedReservationDto() {
        Integer validRestaurantId = 1;
        Integer validTableId = 1;
        Integer validReservationId = 1;
        ReservationDto expectedResult = ReservationTestData.getReservationDtoA();

        when(restaurantRepository.findById(validRestaurantId))
                .thenReturn(Optional.of(RestaurantTestData.getRestaurantEntityA()));
        when(tableRepository.findById(validTableId))
                .thenReturn(Optional.of(TableTestData.getTableEntityA()));
        when(reservationRepository.findById(validReservationId))
                .thenReturn(Optional.of(ReservationTestData.getReservationEntityA()));
        doNothing().when(modelMapper).map(any(UpdateReservationDto.class), any(ReservationEntity.class));
        when(reservationRepository.save(any(ReservationEntity.class)))
                .thenReturn(ReservationTestData.getReservationEntityA());
        when(modelMapper.map(any(ReservationEntity.class), eq(ReservationDto.class)))
                .thenReturn(expectedResult);

        ReservationDto result = underTest.updateReservation(validRestaurantId,
                validTableId,
                validReservationId,
                ReservationTestData.getUpdateReservationDtoA());

        assertThat(expectedResult).isEqualTo(result);
    }

    @Test
    public void updateReservation_invalidReservationId_shouldThrowResourceNotFoundException() {
        Integer validRestaurantId = 1;
        Integer validTableId = 1;
        Integer invalidReservationId = 1;

        when(restaurantRepository.findById(validRestaurantId))
                .thenReturn(Optional.of(RestaurantTestData.getRestaurantEntityA()));
        when(tableRepository.findById(validTableId))
                .thenReturn(Optional.of(TableTestData.getTableEntityA()));
        when(reservationRepository.findById(invalidReservationId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                underTest.updateReservation(validRestaurantId, validTableId, invalidReservationId, ReservationTestData.getUpdateReservationDtoA())
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void updateReservation_invalidTableId_shouldThrowResourceNotFoundException() {
        Integer validRestaurantId = 1;
        Integer invalidTableId = 1;
        Integer shouldNotReachReservationId = 1;

        when(restaurantRepository.findById(validRestaurantId))
                .thenReturn(Optional.of(RestaurantTestData.getRestaurantEntityA()));
        when(tableRepository.findById(invalidTableId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                underTest.updateReservation(validRestaurantId, invalidTableId, shouldNotReachReservationId, ReservationTestData.getUpdateReservationDtoA())
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void updateReservation_invalidRestaurantId_shouldThrowResourceNotFoundException() {
        Integer invalidRestaurantId = 1;
        Integer shouldNotReachTableId = 1;
        Integer shouldNotReachReservationId = 1;

        when(restaurantRepository.findById(invalidRestaurantId))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() ->
                underTest.updateReservation(invalidRestaurantId, shouldNotReachTableId, shouldNotReachReservationId, ReservationTestData.getUpdateReservationDtoA())
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void deleteReservation_validIds_shouldCallDeleteMethod() {
        Integer validRestaurantId = 1;
        Integer validTableId = 1;
        Integer validReservationId = 1;

        when(restaurantRepository.findById(validRestaurantId))
                .thenReturn(Optional.of(RestaurantTestData.getRestaurantEntityA()));
        when(tableRepository.findById(validTableId))
                .thenReturn(Optional.of(TableTestData.getTableEntityA()));
        when(reservationRepository.findById(validReservationId))
                .thenReturn(Optional.of(ReservationTestData.getReservationEntityA()));

        underTest.deleteReservation(validRestaurantId, validTableId, validReservationId);

        verify(reservationRepository, times(1)).deleteById(validReservationId);
    }

    @Test
    public void deleteReservation_invalidReservationId_shouldThrowResourceNotFoundException() {
        Integer validRestaurantId = 1;
        Integer validTableId = 1;
        Integer validReservationId = 1;

        when(restaurantRepository.findById(validRestaurantId))
                .thenReturn(Optional.of(RestaurantTestData.getRestaurantEntityA()));
        when(tableRepository.findById(validTableId))
                .thenReturn(Optional.of(TableTestData.getTableEntityA()));
        when(reservationRepository.findById(validReservationId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                underTest.deleteReservation(validRestaurantId, validTableId, validReservationId)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void deleteReservation_invalidTableId_shouldThrowResourceNotFoundException() {
        Integer validRestaurantId = 1;
        Integer validTableId = 1;
        Integer validReservationId = 1;

        when(restaurantRepository.findById(validRestaurantId))
                .thenReturn(Optional.of(RestaurantTestData.getRestaurantEntityA()));
        when(tableRepository.findById(validTableId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                underTest.deleteReservation(validRestaurantId, validTableId, validReservationId)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void deleteReservation_invalidRestaurantId_shouldThrowResourceNotFoundException() {
        Integer validRestaurantId = 1;
        Integer validTableId = 1;
        Integer validReservationId = 1;

        when(restaurantRepository.findById(validRestaurantId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                underTest.deleteReservation(validRestaurantId, validTableId, validReservationId)
        ).isInstanceOf(ResourceNotFoundException.class);
    }
}
