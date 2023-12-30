package com.vytsablinskas.flavorfare.unit.services;

import com.vytsablinskas.flavorfare.business.exceptions.ResourceNotFoundException;
import com.vytsablinskas.flavorfare.business.services.impl.RestaurantServiceImpl;
import com.vytsablinskas.flavorfare.database.domain.RestaurantEntity;
import com.vytsablinskas.flavorfare.database.repositories.RestaurantRepository;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.RestaurantDto;
import com.vytsablinskas.flavorfare.utils.RestaurantTestData;
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
public class RestaurantServiceImplUnitTests {
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantServiceImpl underTest;

    @Test
    public void getRestaurants_shouldCallDependenciesCorrectAmountOfTimes() {
        when(restaurantRepository.findAll()).thenReturn(Arrays.asList(
                RestaurantEntity.builder().build(),
                RestaurantEntity.builder().build()
        ));

        when(modelMapper.map(any(RestaurantEntity.class), eq(RestaurantDto.class)))
                .thenReturn(RestaurantDto.builder().build());

        underTest.getRestaurants();

        verify(restaurantRepository, times(1)).findAll();
        verify(modelMapper, times(2)).map(any(RestaurantEntity.class), eq(RestaurantDto.class));
    }

    @Test
    public void getRestaurant_validId_shouldReturnCorrectRestaurantDto() {
        RestaurantEntity restaurantEntity = RestaurantTestData.getRestaurantEntityA();
        Optional<RestaurantEntity> optionalResult = Optional.<RestaurantEntity>of(restaurantEntity);
        when(restaurantRepository.findById(restaurantEntity.getId()))
                .thenReturn(optionalResult);
        when(modelMapper.map(any(RestaurantEntity.class), eq(RestaurantDto.class)))
                .thenReturn(RestaurantDto.builder().name(restaurantEntity.getName()).build());

        RestaurantDto result = underTest.getRestaurant(restaurantEntity.getId());

        assertThat(result.getName()).isEqualTo(restaurantEntity.getName());
        verify(modelMapper, times(1))
                .map(any(RestaurantEntity.class), eq(RestaurantDto.class));
    }

    @Test
    public void getRestaurant_invalidId_shouldThrowResourceNotFoundException() {
        Integer invalidId = 1;
        when(restaurantRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> underTest.getRestaurant(invalidId))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
