package com.vytsablinskas.flavorfare.services.impl;

import com.vytsablinskas.flavorfare.database.domain.RestaurantEntity;
import com.vytsablinskas.flavorfare.database.repositories.RestaurantRepository;
import com.vytsablinskas.flavorfare.services.interfaces.RestaurantService;
import com.vytsablinskas.flavorfare.shared.constants.Messages;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.AddRestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.RestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.UpdateRestaurantDto;
import com.vytsablinskas.flavorfare.shared.utils.Result;
import com.vytsablinskas.flavorfare.shared.utils.ResultEntity;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
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
    public ResultEntity<RestaurantDto> getRestaurant(Integer id) {
        Optional<RestaurantEntity> restaurantEntity = repository.findById(id);
        if (restaurantEntity.isEmpty()) {
            return ResultEntity.<RestaurantDto>builder()
                    .isSuccess(false)
                    .message(Messages.GetRestaurantNotFoundMessage(id))
                    .statusCode(HttpStatus.NOT_FOUND)
                    .build();
        }

        RestaurantDto restaurantDto = modelMapper.map(restaurantEntity.get(), RestaurantDto.class);

        return ResultEntity.<RestaurantDto>builder()
                .isSuccess(true)
                .entity(restaurantDto)
                .build();
    }

    @Override
    public RestaurantDto addRestaurant(AddRestaurantDto restaurant) {
        RestaurantEntity restaurantEntity = modelMapper.map(restaurant, RestaurantEntity.class);

        return modelMapper.map(repository.save(restaurantEntity), RestaurantDto.class);
    }

    @Override
    public ResultEntity<RestaurantDto> updateRestaurant(Integer id, UpdateRestaurantDto restaurantUpdateDto) {
        Optional<RestaurantEntity> restaurantEntity = repository.findById(id);
        if (restaurantEntity.isEmpty()) {
            return ResultEntity.<RestaurantDto>builder()
                    .isSuccess(false)
                    .message(Messages.GetRestaurantNotFoundMessage(id))
                    .statusCode(HttpStatus.NOT_FOUND)
                    .build();
        }

        RestaurantEntity updatedRestaurant = repository.save(modelMapper.map(restaurantUpdateDto, RestaurantEntity.class));
        RestaurantDto restaurantDto = modelMapper.map(updatedRestaurant, RestaurantDto.class);

        return ResultEntity.<RestaurantDto>builder()
                .isSuccess(true)
                .entity(restaurantDto)
                .build();
    }

    @Override
    public Result deleteRestaurant(Integer id) {
        Optional<RestaurantEntity> restaurantEntity = repository.findById(id);
        if (restaurantEntity.isEmpty()) {
            return Result.builder()
                    .isSuccess(false)
                    .message(Messages.GetRestaurantNotFoundMessage(id))
                    .statusCode(HttpStatus.NOT_FOUND)
                    .build();
        }

        repository.deleteById(id);

        return Result.builder()
                .isSuccess(true)
                .build();
    }
}