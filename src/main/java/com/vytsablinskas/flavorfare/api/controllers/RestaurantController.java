package com.vytsablinskas.flavorfare.api.controllers;

import com.vytsablinskas.flavorfare.business.services.interfaces.RestaurantService;
import com.vytsablinskas.flavorfare.shared.constants.Prefixes;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.AddRestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.RestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.UpdateRestaurantDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = Prefixes.API_V1)
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping(path = "restaurants")
    @ResponseStatus(HttpStatus.OK)
    public List<RestaurantDto> getRestaurants() {
        return restaurantService.getRestaurants();
    }

    @GetMapping(path = "restaurants/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestaurantDto getRestaurant(@PathVariable Integer id) {
        return restaurantService.getRestaurant(id);
    }

    @PostMapping(path = "restaurants")
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantDto addRestaurant(@RequestBody AddRestaurantDto addRestaurantDto) {
        return restaurantService.addRestaurant(addRestaurantDto);
    }

    @PutMapping(path = "restaurants/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestaurantDto updateRestaurant(@PathVariable Integer id,
                                          @RequestBody UpdateRestaurantDto updateRestaurantDto) {
        return restaurantService.updateRestaurant(id, updateRestaurantDto);
    }

    @DeleteMapping(path = "restaurants/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRestaurant(@PathVariable Integer id) {
        restaurantService.deleteRestaurant(id);
    }
}