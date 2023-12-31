package com.vytsablinskas.flavorfare.unit.services;

import com.vytsablinskas.flavorfare.business.exceptions.ResourceNotFoundException;
import com.vytsablinskas.flavorfare.business.services.impl.TableServiceImpl;
import com.vytsablinskas.flavorfare.database.domain.RestaurantEntity;
import com.vytsablinskas.flavorfare.database.domain.TableEntity;
import com.vytsablinskas.flavorfare.database.repositories.RestaurantRepository;
import com.vytsablinskas.flavorfare.database.repositories.TableRepository;
import com.vytsablinskas.flavorfare.shared.dtos.table.AddTableDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.TableDto;
import com.vytsablinskas.flavorfare.utils.RestaurantTestData;
import com.vytsablinskas.flavorfare.utils.TableTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TableServiceUnitTests {
    @Mock
    private ModelMapper modelMapperMock;

    @Mock
    private RestaurantRepository restaurantRepositoryMock;

    @Mock
    private TableRepository tableRepositoryMock;

    @InjectMocks
    private TableServiceImpl underTest;

    @Test
    public void addRestaurant_validRestaurantId_returnsCreatedTableDto() {
        Integer restaurantId = 1;
        RestaurantEntity restaurantEntity = RestaurantTestData.getRestaurantEntityA();
        AddTableDto addTableDtoA = TableTestData.getAddTableDtoA();
        TableEntity tableEntityA = TableTestData.getTableEntityA();
        Optional<RestaurantEntity> optionalResult = Optional.<RestaurantEntity>of(restaurantEntity);
        TableDto expectedResult = TableTestData.getTableDtoA();

        when(restaurantRepositoryMock.findById(restaurantId))
                .thenReturn(optionalResult);
        when(modelMapperMock.map(any(AddTableDto.class), eq(TableEntity.class)))
                .thenReturn(tableEntityA);
        when(tableRepositoryMock.save(any(TableEntity.class)))
                .thenReturn(tableEntityA);
        when (modelMapperMock.map(any(TableEntity.class), eq(TableDto.class)))
                .thenReturn(expectedResult);

        TableDto result = underTest.addTable(restaurantId, addTableDtoA);

        verify(modelMapperMock, times(1)).map(any(TableEntity.class), eq(TableDto.class));
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void addRestaurant_invalidRestaurantId_shouldThrowResourceNotFoundException() {
        Integer invalidId = 1;
        when(restaurantRepositoryMock.findById(invalidId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                underTest.addTable(invalidId, TableTestData.getAddTableDtoA())
        ).isInstanceOf(ResourceNotFoundException.class);
    }
}
