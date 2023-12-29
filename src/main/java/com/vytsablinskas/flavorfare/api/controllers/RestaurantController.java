package com.vytsablinskas.flavorfare.api.controllers;

import com.vytsablinskas.flavorfare.services.interfaces.RestaurantService;
import com.vytsablinskas.flavorfare.shared.constants.Prefixes;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.AddRestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.RestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.UpdateRestaurantDto;
import com.vytsablinskas.flavorfare.shared.utils.Result;
import com.vytsablinskas.flavorfare.shared.utils.ResultEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = Prefixes.API_V1)
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping(path = "restaurants")
    public ResponseEntity<List<RestaurantDto>> getRestaurants() {
        List<RestaurantDto> restaurants = restaurantService.getRestaurants();

        return ResponseEntity.ok(restaurants);
    }

    @GetMapping(path = "restaurants/{id}")
    public ResponseEntity<?> getRestaurant(@PathVariable Integer id) {
        ResultEntity<RestaurantDto> result = restaurantService.getRestaurant(id);
        if (!result.isSuccess()) {
            return ResponseEntity.status(result.getStatusCode()).body(result.getMessage());
        }

        return ResponseEntity.ok(result.getEntity());
    }

    @PostMapping(path = "restaurants")
    public ResponseEntity<RestaurantDto> addRestaurant(@RequestBody AddRestaurantDto addRestaurantDto) {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(addRestaurantDto);

        return ResponseEntity
                .created(URI.create(String.format("restaurants/%d", restaurantDto.getId())))
                .body(restaurantDto);
    }

    @PutMapping(path = "restaurants/{id}")
    public ResponseEntity<?> updateRestaurant(@PathVariable Integer id, @RequestBody UpdateRestaurantDto updateRestaurantDto) {
        ResultEntity<RestaurantDto> result = restaurantService.updateRestaurant(id, updateRestaurantDto);
        if (!result.isSuccess()) {
            return ResponseEntity.status(result.getStatusCode()).body(result.getMessage());
        }

        return ResponseEntity.ok(result.getEntity());
    }

    @DeleteMapping(path = "restaurants/{id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Integer id) {
        Result result = restaurantService.deleteRestaurant(id);
        if (!result.isSuccess()) {
            return ResponseEntity.status(result.getStatusCode()).body(result.getMessage());
        }

        return ResponseEntity
                .noContent()
                .build();
    }
}