package com.vytsablinskas.flavorfare.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vytsablinskas.flavorfare.api.controllers.RestaurantController;
import com.vytsablinskas.flavorfare.business.services.interfaces.RestaurantService;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.AddRestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.RestaurantDto;
import com.vytsablinskas.flavorfare.utils.RestaurantTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class RestaurantControllerIntegrationTests {
    private final MockMvc mockMvc;
    private final RestaurantService restaurantService;

    private final ObjectMapper objectMapper;

    @Autowired
    public RestaurantControllerIntegrationTests(MockMvc mockMvc, RestaurantService restaurantService, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.restaurantService = restaurantService;
        this.objectMapper = objectMapper;
    }

    @Test
    public void getRestaurants_shouldReturnHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getRestaurants_shouldReturnListOfRestaurants() throws Exception {
        AddRestaurantDto addRestaurantDtoA = RestaurantTestData.getAddRestaurantDtoA();
        RestaurantDto restaurantDto = restaurantService.addRestaurant(addRestaurantDtoA);
        String restaurantDtoJson = objectMapper.writeValueAsString(restaurantDto);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(restaurantDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value(addRestaurantDtoA.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].address").value(addRestaurantDtoA.getAddress())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].openingTime").value(addRestaurantDtoA.getOpeningTime().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].closingTime").value(addRestaurantDtoA.getClosingTime().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].intervalBetweenBookings").value(addRestaurantDtoA.getIntervalBetweenBookings().toString())
        );
    }
}