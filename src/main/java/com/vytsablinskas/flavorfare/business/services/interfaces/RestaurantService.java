package com.vytsablinskas.flavorfare.business.services.interfaces;

import com.vytsablinskas.flavorfare.business.exceptions.ResourceNotFoundException;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.AddRestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.RestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.UpdateRestaurantDto;
import com.vytsablinskas.flavorfare.shared.utils.Result;
import com.vytsablinskas.flavorfare.shared.utils.ResultEntity;

import java.util.List;

public interface RestaurantService {
    List<RestaurantDto> getRestaurants();

    RestaurantDto getRestaurant(Integer id);

    RestaurantDto addRestaurant(AddRestaurantDto restaurant);

    RestaurantDto updateRestaurant(Integer id, UpdateRestaurantDto restaurantUpdateDto);

    void deleteRestaurant(Integer id);
}