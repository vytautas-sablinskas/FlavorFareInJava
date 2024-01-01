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
import com.vytsablinskas.flavorfare.utils.data.RestaurantTestData;
import com.vytsablinskas.flavorfare.utils.data.TableTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

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

        when(restaurantRepositoryMock.findById(validRestaurantId))
                .thenReturn(Optional.<RestaurantEntity>of(RestaurantTestData.getRestaurantEntityWithEntityATable()));
        when(tableRepositoryMock.findByRestaurantId(validRestaurantId))
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
        TableDto expectedTable = TableTestData.getTableDtoA();

        when(restaurantRepositoryMock.findById(validRestaurantId))
                .thenReturn(Optional.<RestaurantEntity>of(RestaurantTestData.getRestaurantEntityA()));

        when(tableRepositoryMock.findById(validTableId))
                .thenReturn(Optional.<TableEntity>of(TableTestData.getTableEntityA()));

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

        when(restaurantRepositoryMock.findById(validRestaurantId))
                .thenReturn(Optional.<RestaurantEntity>of(RestaurantTestData.getRestaurantEntityA()));
        when(tableRepositoryMock.findById(invalidTableId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                underTest.getTable(validRestaurantId, invalidTableId)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void addTable_validRestaurantId_returnsCreatedTableDto() {
        Integer restaurantId = 1;
        TableEntity tableEntityA = TableTestData.getTableEntityA();
        TableDto expectedResult = TableTestData.getTableDtoA();

        when(restaurantRepositoryMock.findById(restaurantId))
                .thenReturn(Optional.<RestaurantEntity>of(RestaurantTestData.getRestaurantEntityA()));
        when(modelMapperMock.map(any(AddTableDto.class), eq(TableEntity.class)))
                .thenReturn(tableEntityA);
        when(tableRepositoryMock.save(any(TableEntity.class)))
                .thenReturn(tableEntityA);
        when (modelMapperMock.map(any(TableEntity.class), eq(TableDto.class)))
                .thenReturn(expectedResult);

        TableDto result = underTest.addTable(restaurantId, TableTestData.getAddTableDtoA());

        verify(modelMapperMock, times(1)).map(any(TableEntity.class), eq(TableDto.class));
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void addTable_validIdsButAlreadyContainsThatSizeTable_throwsTableSizeAlreadyInDatabaseException() {
        Integer restaurantId = 1;
        AddTableDto addTableDtoA = TableTestData.getAddTableDtoA();
        addTableDtoA.setSize(TableTestData.getTableEntityA().getSize());

        when(restaurantRepositoryMock.findById(restaurantId))
                .thenReturn(Optional.<RestaurantEntity>of(RestaurantTestData.getRestaurantEntityWithEntityATable()));
        when(tableRepositoryMock.findByRestaurantId(restaurantId))
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
        TableEntity tableEntity = TableTestData.getTableEntityA();
        TableDto expectedTable = TableTestData.getTableDtoA();

        when(restaurantRepositoryMock.findById(validRestaurantId))
                .thenReturn(Optional.<RestaurantEntity>of(RestaurantTestData.getRestaurantEntityA()));
        when(tableRepositoryMock.findById(validTableId))
                .thenReturn(Optional.<TableEntity>of(tableEntity));
        doNothing().when(modelMapperMock).map(any(UpdateTableDto.class), any(TableEntity.class));
        when(tableRepositoryMock.save(any(TableEntity.class)))
                .thenReturn(tableEntity);
        when(modelMapperMock.map(any(TableEntity.class), eq(TableDto.class)))
                .thenReturn(expectedTable);

        TableDto result = underTest.updateTable(validRestaurantId, validTableId, TableTestData.getUpdateTableDtoA());

        verify(modelMapperMock, times(1)).map(any(TableEntity.class), eq(TableDto.class));
        assertThat(result).isEqualTo(expectedTable);
    }

    @Test
    public void updateTable_validIdsButDuplicateSize_throwsTableSizeAlreadyInDatabaseException() {
        Integer validRestaurantId = 1;
        TableEntity tableEntityA = TableTestData.getTableEntityA();
        UpdateTableDto updateTableDtoA = TableTestData.getUpdateTableDtoA();
        updateTableDtoA.setSize(tableEntityA.getSize());
        Integer tableIdToUpdate = tableEntityA.getTableId() + 1;

        when(restaurantRepositoryMock.findById(validRestaurantId))
                .thenReturn(Optional.<RestaurantEntity>of(RestaurantTestData.getRestaurantEntityWithEntityATable()));
        when(tableRepositoryMock.findById(tableIdToUpdate))
                .thenReturn(Optional.<TableEntity>of(tableEntityA));
        when(tableRepositoryMock.findByRestaurantId(validRestaurantId))
                .thenReturn(TableTestData.getTableEntityListA());

        assertThatThrownBy(() ->
                underTest.updateTable(validRestaurantId, tableIdToUpdate, updateTableDtoA)
        ).isInstanceOf(TableSizeAlreadyInDatabaseException.class);
    }

    @Test
    public void updateTable_invalidTableId_throwsResourceNotFoundException() {
        Integer validRestaurantId = 1;
        Integer invalidTableId = 1;

        when(restaurantRepositoryMock.findById(validRestaurantId))
                .thenReturn(Optional.<RestaurantEntity>of(RestaurantTestData.getRestaurantEntityWithEntityATable()));
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

        when(restaurantRepositoryMock.findById(validRestaurantId))
                .thenReturn(Optional.<RestaurantEntity>of(RestaurantTestData.getRestaurantEntityWithEntityATable()));
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