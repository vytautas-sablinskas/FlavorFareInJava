package com.vytsablinskas.flavorfare.business.services.impl;

import com.vytsablinskas.flavorfare.business.exceptions.ResourceNotFoundException;
import com.vytsablinskas.flavorfare.business.services.interfaces.RestaurantService;
import com.vytsablinskas.flavorfare.database.domain.RestaurantEntity;
import com.vytsablinskas.flavorfare.database.repositories.RestaurantRepository;
import com.vytsablinskas.flavorfare.shared.constants.Messages;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.AddRestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.RestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.UpdateRestaurantDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository repository;
    private final ModelMapper modelMapper;

    public RestaurantServiceImpl(RestaurantRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<RestaurantDto> getRestaurants() {
        List<RestaurantEntity> restaurantEntities = StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .toList();

        return restaurantEntities
                .stream()
                .map(entity -> modelMapper.map(entity, RestaurantDto.class))
                .toList();
    }

    @Override
    public RestaurantDto getRestaurant(Integer id) {
        Optional<RestaurantEntity> restaurantEntity = repository.findById(id);
        if (restaurantEntity.isEmpty()) {
            throw new ResourceNotFoundException(Messages.getRestaurantNotFoundMessage(id));
        }

        return modelMapper.map(restaurantEntity.get(), RestaurantDto.class);
    }

    @Override
    public RestaurantDto addRestaurant(AddRestaurantDto restaurant) {
        RestaurantEntity restaurantEntity = modelMapper.map(restaurant, RestaurantEntity.class);

        return modelMapper.map(repository.save(restaurantEntity), RestaurantDto.class);
    }

    @Override
    public RestaurantDto updateRestaurant(Integer id, UpdateRestaurantDto restaurantUpdateDto) {
        Optional<RestaurantEntity> restaurantEntity = repository.findById(id);
        if (restaurantEntity.isEmpty()) {
            throw new ResourceNotFoundException(Messages.getRestaurantNotFoundMessage(id));
        }

        RestaurantEntity updatedRestaurant = repository.save(modelMapper.map(restaurantUpdateDto, RestaurantEntity.class));

        return modelMapper.map(updatedRestaurant, RestaurantDto.class);
    }

    @Override
    public void deleteRestaurant(Integer id) {
        Optional<RestaurantEntity> restaurantEntity = repository.findById(id);
        if (restaurantEntity.isEmpty()) {
            throw new ResourceNotFoundException(Messages.getRestaurantNotFoundMessage(id));
        }

        repository.deleteById(id);
    }
}