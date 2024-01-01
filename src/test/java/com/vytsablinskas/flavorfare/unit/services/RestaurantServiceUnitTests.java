package com.vytsablinskas.flavorfare.unit.services;

import com.vytsablinskas.flavorfare.business.exceptions.ResourceNotFoundException;
import com.vytsablinskas.flavorfare.business.services.impl.RestaurantServiceImpl;
import com.vytsablinskas.flavorfare.database.domain.RestaurantEntity;
import com.vytsablinskas.flavorfare.database.repositories.RestaurantRepository;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.AddRestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.RestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.UpdateRestaurantDto;
import com.vytsablinskas.flavorfare.utils.data.RestaurantTestData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceUnitTests {
    @Mock
    private ModelMapper modelMapperMock;

    @Mock
    private RestaurantRepository restaurantRepositoryMock;

    @InjectMocks
    private RestaurantServiceImpl underTest;

    @Test
    public void getRestaurants_shouldCallDependenciesCorrectAmountOfTimes() {
        when(restaurantRepositoryMock.findAll()).thenReturn(Arrays.asList(
                RestaurantEntity.builder().build(),
                RestaurantEntity.builder().build()
        ));
        when(modelMapperMock.map(any(RestaurantEntity.class), eq(RestaurantDto.class)))
                .thenReturn(RestaurantDto.builder().build());

        underTest.getRestaurants();

        verify(restaurantRepositoryMock, times(1)).findAll();
        verify(modelMapperMock, times(2)).map(any(RestaurantEntity.class), eq(RestaurantDto.class));
    }

    @Test
    public void getRestaurant_validId_shouldReturnCorrectRestaurantDto() {
        RestaurantEntity restaurantEntity = RestaurantTestData.getRestaurantEntityA();

        when(restaurantRepositoryMock.findById(restaurantEntity.getRestaurantId()))
                .thenReturn(Optional.<RestaurantEntity>of(restaurantEntity));
        when(modelMapperMock.map(any(RestaurantEntity.class), eq(RestaurantDto.class)))
                .thenReturn(RestaurantDto.builder().name(restaurantEntity.getName()).build());

        RestaurantDto result = underTest.getRestaurant(restaurantEntity.getRestaurantId());

        assertThat(result.getName()).isEqualTo(restaurantEntity.getName());
        verify(modelMapperMock, times(1))
                .map(any(RestaurantEntity.class), eq(RestaurantDto.class));
    }

    @Test
    public void getRestaurant_invalidId_shouldThrowResourceNotFoundException() {
        Integer invalidId = 1;
        when(restaurantRepositoryMock.findById(invalidId))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> underTest.getRestaurant(invalidId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void addRestaurant_shouldCallDependencies() {
        RestaurantEntity restaurantEntity = RestaurantTestData.getRestaurantEntityA();
        RestaurantDto restaurantDtoA = RestaurantTestData.getRestaurantDtoA();

        when(modelMapperMock.map(any(AddRestaurantDto.class), eq(RestaurantEntity.class)))
                .thenReturn(restaurantEntity);
        when(restaurantRepositoryMock.save(any(RestaurantEntity.class)))
                .thenReturn(restaurantEntity);
        when(modelMapperMock.map(any(RestaurantEntity.class), eq(RestaurantDto.class)))
                .thenReturn(restaurantDtoA);

        RestaurantDto result = underTest.addRestaurant(RestaurantTestData.getAddRestaurantDtoB());

        verify(modelMapperMock, times(1)).map(any(RestaurantEntity.class), eq(RestaurantDto.class));
        assertThat(result).isEqualTo(restaurantDtoA);
    }

    @Test
    public void updateRestaurant_validId_shouldCallDependenciesAndReturnUpdatedRestaurant() {
        Integer idToUpdate = 1;
        RestaurantEntity restaurantEntity = RestaurantTestData.getRestaurantEntityA();
        RestaurantDto expectedResult = RestaurantTestData.getRestaurantDtoA();

        when(restaurantRepositoryMock.findById(idToUpdate))
                .thenReturn(Optional.<RestaurantEntity>of(restaurantEntity));
        doNothing().when(modelMapperMock).map(any(UpdateRestaurantDto.class), any(RestaurantEntity.class));
        when(restaurantRepositoryMock.save(any(RestaurantEntity.class)))
                .thenReturn(restaurantEntity);
        when(modelMapperMock.map(any(RestaurantEntity.class), eq(RestaurantDto.class)))
                .thenReturn(expectedResult);

        RestaurantDto result = underTest.updateRestaurant(idToUpdate, RestaurantTestData.getUpdateRestaurantDtoA());

        verify(modelMapperMock, times(1)).map(any(RestaurantEntity.class), eq(RestaurantDto.class));
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void updateRestaurant_invalidId_shouldThrowResourceNotFoundException() {
        Integer invalidId = 1;

        when(restaurantRepositoryMock.findById(invalidId))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> underTest.updateRestaurant(invalidId, RestaurantTestData.getUpdateRestaurantDtoA()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void deleteRestaurant_validId_shouldCallDependencies() {
        Integer validId = 1;

        when(restaurantRepositoryMock.findById(validId))
                .thenReturn(Optional.<RestaurantEntity>of(RestaurantTestData.getRestaurantEntityA()));

        underTest.deleteRestaurant(validId);

        verify(restaurantRepositoryMock, times(1)).deleteById(validId);
    }

    @Test
    public void deleteRestaurant_invalidId_shouldThrowResourceNotFoundException() {
        Integer invalidId = 1;

        when(restaurantRepositoryMock.findById(invalidId))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> underTest.deleteRestaurant(invalidId))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}