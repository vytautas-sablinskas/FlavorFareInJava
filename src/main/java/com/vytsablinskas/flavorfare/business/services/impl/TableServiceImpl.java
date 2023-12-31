package com.vytsablinskas.flavorfare.business.services.impl;

import com.vytsablinskas.flavorfare.business.exceptions.ResourceNotFoundException;
import com.vytsablinskas.flavorfare.business.services.interfaces.TableService;
import com.vytsablinskas.flavorfare.database.domain.RestaurantEntity;
import com.vytsablinskas.flavorfare.database.domain.TableEntity;
import com.vytsablinskas.flavorfare.database.repositories.RestaurantRepository;
import com.vytsablinskas.flavorfare.database.repositories.TableRepository;
import com.vytsablinskas.flavorfare.shared.constants.Messages;
import com.vytsablinskas.flavorfare.shared.dtos.table.AddTableDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.TableDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TableServiceImpl implements TableService {
    private ModelMapper modelMapper;
    private TableRepository tableRepository;
    private RestaurantRepository restaurantRepository;

    public TableServiceImpl(ModelMapper modelMapper, TableRepository tableRepository, RestaurantRepository restaurantRepository) {
        this.modelMapper = modelMapper;
        this.tableRepository = tableRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public TableDto addTable(Integer restaurantId, AddTableDto addTableDto) {
        Optional<RestaurantEntity> restaurantEntity = restaurantRepository.findById(restaurantId);
        if (restaurantEntity.isEmpty()) {
            throw new ResourceNotFoundException(Messages.getRestaurantNotFoundMessage(restaurantId));
        }

        TableEntity tableEntityToAdd = modelMapper.map(addTableDto, TableEntity.class);
        tableEntityToAdd.setRestaurant(restaurantEntity.get());
        TableEntity createdTable = tableRepository.save(tableEntityToAdd);

        return modelMapper.map(createdTable, TableDto.class);
    }
}
