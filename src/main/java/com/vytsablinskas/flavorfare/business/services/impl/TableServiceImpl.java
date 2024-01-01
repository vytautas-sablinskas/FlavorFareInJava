package com.vytsablinskas.flavorfare.business.services.impl;

import com.vytsablinskas.flavorfare.business.exceptions.ResourceNotFoundException;
import com.vytsablinskas.flavorfare.business.exceptions.TableSizeAlreadyInDatabaseException;
import com.vytsablinskas.flavorfare.business.services.interfaces.TableService;
import com.vytsablinskas.flavorfare.database.domain.RestaurantEntity;
import com.vytsablinskas.flavorfare.database.domain.TableEntity;
import com.vytsablinskas.flavorfare.database.repositories.RestaurantRepository;
import com.vytsablinskas.flavorfare.database.repositories.TableRepository;
import com.vytsablinskas.flavorfare.shared.constants.Messages;
import com.vytsablinskas.flavorfare.shared.dtos.table.AddTableDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.TableDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.UpdateTableDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TableServiceImpl implements TableService {
    private final ModelMapper modelMapper;
    private final TableRepository tableRepository;
    private final RestaurantRepository restaurantRepository;

    public TableServiceImpl(ModelMapper modelMapper, TableRepository tableRepository, RestaurantRepository restaurantRepository) {
        this.modelMapper = modelMapper;
        this.tableRepository = tableRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<TableDto> getTables(Integer restaurantId) {
        Optional<RestaurantEntity> restaurantEntity = restaurantRepository.findById(restaurantId);
        if (restaurantEntity.isEmpty()) {
            throw new ResourceNotFoundException(Messages.getRestaurantNotFoundMessage(restaurantId));
        }

        return tableRepository
                .findByRestaurantId(restaurantId)
                .stream()
                .map(entity -> modelMapper.map(entity, TableDto.class))
                .toList();
    }

    @Override
    public TableDto getTable(Integer restaurantId, Integer tableId) {
        Optional<RestaurantEntity> restaurantEntity = restaurantRepository.findById(restaurantId);
        if (restaurantEntity.isEmpty()) {
            throw new ResourceNotFoundException(Messages.getRestaurantNotFoundMessage(restaurantId));
        }

        Optional<TableEntity> tableEntity = tableRepository.findById(tableId);
        if (tableEntity.isEmpty()) {
            throw new ResourceNotFoundException(Messages.getTableNotFoundMessage(tableId));
        }

        return modelMapper.map(tableEntity.get(), TableDto.class);
    }

    @Override
    public TableDto addTable(Integer restaurantId, AddTableDto addTableDto) {
        Optional<RestaurantEntity> restaurantEntity = restaurantRepository.findById(restaurantId);
        if (restaurantEntity.isEmpty()) {
            throw new ResourceNotFoundException(Messages.getRestaurantNotFoundMessage(restaurantId));
        }

        List<TableEntity> currentTablesInsideRestaurant = tableRepository.findByRestaurantId(restaurantId);
        for (TableEntity table : currentTablesInsideRestaurant) {
            if (table.getSize().equals(addTableDto.getSize())) {
                throw new TableSizeAlreadyInDatabaseException(Messages.getTableOfSizeIsDuplicateMessage(addTableDto.getSize()));
            }
        }

        TableEntity tableEntityToAdd = modelMapper.map(addTableDto, TableEntity.class);
        tableEntityToAdd.setRestaurant(restaurantEntity.get());
        TableEntity createdTable = tableRepository.save(tableEntityToAdd);

        return modelMapper.map(createdTable, TableDto.class);
    }

    @Override
    public TableDto updateTable(Integer restaurantId, Integer tableId, UpdateTableDto updateTableDto) {
        Optional<RestaurantEntity> restaurantEntity = restaurantRepository.findById(restaurantId);
        if (restaurantEntity.isEmpty()) {
            throw new ResourceNotFoundException(Messages.getRestaurantNotFoundMessage(restaurantId));
        }

        Optional<TableEntity> optionalTableEntity = tableRepository.findById(tableId);
        if (optionalTableEntity.isEmpty()) {
            throw new ResourceNotFoundException(Messages.getTableNotFoundMessage(tableId));
        }

        List<TableEntity> currentTablesInsideRestaurant = tableRepository.findByRestaurantId(restaurantId);
        for (TableEntity table : currentTablesInsideRestaurant) {
            if (table.getSize().equals(updateTableDto.getSize()) &&
                !table.getTableId().equals(tableId)) {
                throw new TableSizeAlreadyInDatabaseException(Messages.getTableOfSizeIsDuplicateMessage(updateTableDto.getSize()));
            }
        }

        TableEntity tableEntity = optionalTableEntity.get();
        modelMapper.map(updateTableDto, tableEntity);

        TableEntity updatedEntity = tableRepository.save(tableEntity);

        return modelMapper.map(updatedEntity, TableDto.class);
    }

    @Override
    public void deleteTable(Integer restaurantId, Integer tableId) {
        Optional<RestaurantEntity> restaurantEntity = restaurantRepository.findById(restaurantId);
        if (restaurantEntity.isEmpty()) {
            throw new ResourceNotFoundException(Messages.getRestaurantNotFoundMessage(restaurantId));
        }

        Optional<TableEntity> optionalTableEntity = tableRepository.findById(tableId);
        if (optionalTableEntity.isEmpty()) {
            throw new ResourceNotFoundException(Messages.getTableNotFoundMessage(tableId));
        }

        tableRepository.deleteById(tableId);
    }
}