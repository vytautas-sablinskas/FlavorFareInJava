package com.vytsablinskas.flavorfare.unit.services;

import com.vytsablinskas.flavorfare.business.exceptions.ResourceNotFoundException;
import com.vytsablinskas.flavorfare.business.exceptions.TableSizeAlreadyInDatabaseException;
import com.vytsablinskas.flavorfare.business.services.impl.TableServiceImpl;
import com.vytsablinskas.flavorfare.database.domain.RestaurantEntity;
import com.vytsablinskas.flavorfare.database.domain.TableEntity;
import com.vytsablinskas.flavorfare.database.repositories.RestaurantRepository;
import com.vytsablinskas.flavorfare.database.repositories.TableRepository;
import com.vytsablinskas.flavorfare.shared.dtos.table.AddTableDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.TableDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.UpdateTableDto;
import com.vytsablinskas.flavorfare.utils.RestaurantTestData;
import com.vytsablinskas.flavorfare.utils.TableTestData;
import jakarta.persistence.Table;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
    public void getTables_validRestaurantId_getsTablesAndMapsToTableDtoList() {
        Integer validRestaurantId = 1;
        RestaurantEntity restaurantEntity = RestaurantTestData.getRestaurantEntityWithEntityATable();
        Optional<RestaurantEntity> optionalResult = Optional.<RestaurantEntity>of(restaurantEntity);

        when(restaurantRepositoryMock.findById(validRestaurantId))
                .thenReturn(optionalResult);
        when(tableRepositoryMock.findByCondition(validRestaurantId))
                .thenReturn(TableTestData.getTableEntityListA());
        when(modelMapperMock.map(any(TableEntity.class), eq(TableDto.class)))
                .thenReturn(TableDto.builder().build());

        List<TableDto> tables = underTest.getTables(validRestaurantId);

        verify(modelMapperMock, times(1)).map(any(TableEntity.class), eq(TableDto.class));
        assertThat(tables).hasSize(1);
    }

    @Test
    public void getTables_invalidRestaurantId_throwsResourceNotFoundException() {
        Integer invalidRestaurantId = 1;

        when(restaurantRepositoryMock.findById(invalidRestaurantId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                underTest.getTables(invalidRestaurantId)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void getTable_validRestaurantAndTableId_returnsCorrectTable() {
        Integer validRestaurantId = 1;
        Integer validTableId = 1;
        RestaurantEntity restaurantEntity = RestaurantTestData.getRestaurantEntityA();
        Optional<RestaurantEntity> optionalGoodRestaurantResult = Optional.<RestaurantEntity>of(restaurantEntity);
        TableEntity tableEntity = TableTestData.getTableEntityA();
        Optional<TableEntity> optionalGoodTableResult = Optional.<TableEntity>of(tableEntity);
        TableDto expectedTable = TableTestData.getTableDtoA();

        when(restaurantRepositoryMock.findById(validRestaurantId))
                .thenReturn(optionalGoodRestaurantResult);

        when(tableRepositoryMock.findById(validTableId))
                .thenReturn(optionalGoodTableResult);

        when(modelMapperMock.map(any(TableEntity.class), eq(TableDto.class)))
                .thenReturn(expectedTable);

        TableDto result = underTest.getTable(validRestaurantId, validTableId);

        assertThat(result).isEqualTo(expectedTable);
        verify(modelMapperMock, times(1)).map(any(TableEntity.class), eq(TableDto.class));
    }

    @Test
    public void getTable_invalidRestaurantId_throwsResourceNotFoundException() {
        Integer invalidRestaurantId = 1;

        when(restaurantRepositoryMock.findById(invalidRestaurantId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                underTest.getTables(invalidRestaurantId)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void getTable_invalidTableId_throwsResourceNotFoundException() {
        Integer validRestaurantId = 1;
        Integer invalidTableId = 1;
        RestaurantEntity restaurantEntity = RestaurantTestData.getRestaurantEntityA();
        Optional<RestaurantEntity> optionalGoodRestaurantResult = Optional.<RestaurantEntity>of(restaurantEntity);

        when(restaurantRepositoryMock.findById(validRestaurantId))
                .thenReturn(optionalGoodRestaurantResult);

        when(tableRepositoryMock.findById(invalidTableId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                underTest.getTable(validRestaurantId, invalidTableId)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void addTable_validRestaurantId_returnsCreatedTableDto() {
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
    public void addTable_validIdsButAlreadyContainsThatSizeTable_throwsTableSizeAlreadyInDatabaseException() {
        Integer restaurantId = 1;
        AddTableDto addTableDtoA = TableTestData.getAddTableDtoA();
        RestaurantEntity restaurantEntity = RestaurantTestData.getRestaurantEntityWithEntityATable();
        Optional<RestaurantEntity> optionalResult = Optional.<RestaurantEntity>of(restaurantEntity);
        addTableDtoA.setSize(TableTestData.getTableEntityA().getSize());


        when(restaurantRepositoryMock.findById(restaurantId))
                .thenReturn(optionalResult);
        when(tableRepositoryMock.findByCondition(restaurantId))
                .thenReturn(TableTestData.getTableEntityListA());

        assertThatThrownBy(() ->
                underTest.addTable(restaurantId, addTableDtoA)
        ).isInstanceOf(TableSizeAlreadyInDatabaseException.class);
    }

    @Test
    public void addTable_invalidRestaurantId_shouldThrowResourceNotFoundException() {
        Integer invalidId = 1;
        when(restaurantRepositoryMock.findById(invalidId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                underTest.addTable(invalidId, TableTestData.getAddTableDtoA())
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void updateTable_validIdsAndNoDuplicateSize_returnsUpdatedTable() {
        Integer validRestaurantId = 1;
        Integer validTableId = 1;
        RestaurantEntity restaurantEntity = RestaurantTestData.getRestaurantEntityA();
        Optional<RestaurantEntity> optionalGoodRestaurantResult = Optional.<RestaurantEntity>of(restaurantEntity);
        TableEntity tableEntity = TableTestData.getTableEntityA();
        Optional<TableEntity> optionalGoodTableResult = Optional.<TableEntity>of(tableEntity);
        UpdateTableDto updateTableDtoA = TableTestData.getUpdateTableDtoA();
        TableDto expectedTable = TableTestData.getTableDtoA();

        when(restaurantRepositoryMock.findById(validRestaurantId))
                .thenReturn(optionalGoodRestaurantResult);
        when(tableRepositoryMock.findById(validTableId))
                .thenReturn(optionalGoodTableResult);
        doNothing().when(modelMapperMock).map(any(UpdateTableDto.class), any(TableEntity.class));
        when(tableRepositoryMock.save(any(TableEntity.class)))
                .thenReturn(tableEntity);
        when(modelMapperMock.map(any(TableEntity.class), eq(TableDto.class)))
                .thenReturn(expectedTable);

        TableDto result = underTest.updateTable(validRestaurantId, validTableId, updateTableDtoA);

        verify(modelMapperMock, times(1)).map(any(TableEntity.class), eq(TableDto.class));
        assertThat(result).isEqualTo(expectedTable);
    }

    @Test
    public void updateTable_validIdsButDuplicateSize_throwsTableSizeAlreadyInDatabaseException() {
        Integer validRestaurantId = 1;
        Optional<RestaurantEntity> goodRestaurant = Optional.<RestaurantEntity>of(RestaurantTestData.getRestaurantEntityWithEntityATable());
        TableEntity tableEntityA = TableTestData.getTableEntityA();
        Optional<TableEntity> goodTable = Optional.<TableEntity>of(tableEntityA);
        UpdateTableDto updateTableDtoA = TableTestData.getUpdateTableDtoA();
        updateTableDtoA.setSize(tableEntityA.getSize());
        Integer tableIdToUpdate = tableEntityA.getTableId() + 1;

        when(restaurantRepositoryMock.findById(validRestaurantId))
                .thenReturn(goodRestaurant);
        when(tableRepositoryMock.findById(tableIdToUpdate))
                .thenReturn(goodTable);
        when(tableRepositoryMock.findByCondition(validRestaurantId))
                .thenReturn(TableTestData.getTableEntityListA());

        assertThatThrownBy(() ->
                underTest.updateTable(validRestaurantId, tableIdToUpdate, updateTableDtoA)
        ).isInstanceOf(TableSizeAlreadyInDatabaseException.class);
    }

    @Test
    public void updateTable_invalidTableId_throwsResourceNotFoundException() {
        Integer validRestaurantId = 1;
        Integer invalidTableId = 1;
        Optional<RestaurantEntity> goodRestaurant = Optional.<RestaurantEntity>of(RestaurantTestData.getRestaurantEntityWithEntityATable());
        when(restaurantRepositoryMock.findById(validRestaurantId))
                .thenReturn(goodRestaurant);
        when(tableRepositoryMock.findById(invalidTableId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                underTest.updateTable(validRestaurantId, invalidTableId, TableTestData.getUpdateTableDtoA())
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void updateTable_invalidRestaurantId_throwsResourceNotFoundException() {
        Integer invalidRestaurantId = 1;
        Integer validTableId = 1;

        when(restaurantRepositoryMock.findById(invalidRestaurantId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                underTest.updateTable(invalidRestaurantId, validTableId, TableTestData.getUpdateTableDtoA())
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void deleteTable_validIds_shouldCallDeleteMethod() {
        Integer validRestaurantId = 1;
        Integer validTableId = 1;

        when(restaurantRepositoryMock.findById(validRestaurantId))
                .thenReturn(Optional.<RestaurantEntity>of(RestaurantTestData.getRestaurantEntityWithEntityATable()));
        when(tableRepositoryMock.findById(validTableId))
                .thenReturn(Optional.<TableEntity>of(TableTestData.getTableEntityA()));

        underTest.deleteTable(validRestaurantId, validTableId);

        verify(tableRepositoryMock, times(1)).deleteById(validTableId);
    }

    @Test
    public void deleteTable_invalidTableId_throwsResourceNotFoundException() {
        Integer validRestaurantId = 1;
        Integer invalidTableId = 1;
        Optional<RestaurantEntity> goodRestaurant = Optional.<RestaurantEntity>of(RestaurantTestData.getRestaurantEntityWithEntityATable());
        when(restaurantRepositoryMock.findById(validRestaurantId))
                .thenReturn(goodRestaurant);
        when(tableRepositoryMock.findById(invalidTableId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                underTest.deleteTable(validRestaurantId, invalidTableId)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void deleteTable_invalidRestaurantId_throwsResourceNotFoundException() {
        Integer invalidRestaurantId = 1;
        Integer validTableId = 1;

        when(restaurantRepositoryMock.findById(invalidRestaurantId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                underTest.deleteTable(invalidRestaurantId, validTableId)
        ).isInstanceOf(ResourceNotFoundException.class);
    }
}