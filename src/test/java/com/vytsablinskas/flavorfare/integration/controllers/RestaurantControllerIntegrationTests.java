package com.vytsablinskas.flavorfare.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vytsablinskas.flavorfare.business.services.interfaces.RestaurantService;
import com.vytsablinskas.flavorfare.shared.constants.Messages;
import com.vytsablinskas.flavorfare.shared.constants.endpoints.RestaurantEndpoints;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.AddRestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.RestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.UpdateRestaurantDto;
import com.vytsablinskas.flavorfare.utils.data.RestaurantTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@WithMockUser
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
                MockMvcRequestBuilders.get(RestaurantEndpoints.restaurants)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getRestaurants_shouldReturnListOfRestaurants() throws Exception {
        AddRestaurantDto addRestaurantDtoA = RestaurantTestData.getAddRestaurantDtoA();
        RestaurantDto restaurantDto = restaurantService.addRestaurant(addRestaurantDtoA);
        String restaurantDtoJson = objectMapper.writeValueAsString(restaurantDto);

        mockMvc.perform(
                MockMvcRequestBuilders.get(RestaurantEndpoints.restaurants)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(restaurantDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").value(restaurantDto.getId())
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

    @Test
    public void getRestaurant_validId_shouldReturnHttpStatus200() throws Exception {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());

        mockMvc.perform(
                MockMvcRequestBuilders.get(RestaurantEndpoints.restaurant(restaurantDto.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void getRestaurant_validId_shouldReturnRestaurantDtoInformation() throws Exception {
        AddRestaurantDto addRestaurantDtoA = RestaurantTestData.getAddRestaurantDtoA();
        RestaurantDto restaurantDto = restaurantService.addRestaurant(addRestaurantDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.get(RestaurantEndpoints.restaurant(restaurantDto.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(restaurantDto.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(addRestaurantDtoA.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.address").value(addRestaurantDtoA.getAddress())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.openingTime").value(addRestaurantDtoA.getOpeningTime().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.closingTime").value(addRestaurantDtoA.getClosingTime().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.intervalBetweenBookings").value(addRestaurantDtoA.getIntervalBetweenBookings().toString())
        );
    }

    @Test
    public void getRestaurant_invalidId_shouldReturnHttpStatus404WithErrorMessageInBody() throws Exception {
        Integer invalidId = 1;
        String expectedErrorMessage = Messages.getRestaurantNotFoundMessage(invalidId);

        mockMvc.perform(
                MockMvcRequestBuilders.get(RestaurantEndpoints.restaurant(invalidId))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.content().string(expectedErrorMessage)
        );
    }

    @Test
    public void addRestaurant_shouldReturnHttpStatus201() throws Exception {
        AddRestaurantDto addRestaurantDtoA = RestaurantTestData.getAddRestaurantDtoA();
        String addRestaurantDtoJson = objectMapper.writeValueAsString(addRestaurantDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post(RestaurantEndpoints.addRestaurant)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addRestaurantDtoJson)
                        .with(csrf())
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void addRestaurant_shouldReturnCreatedRestaurantDto() throws Exception {
        AddRestaurantDto addRestaurantDto = RestaurantTestData.getAddRestaurantDtoA();
        String addRestaurantDtoJson = objectMapper.writeValueAsString(addRestaurantDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post(RestaurantEndpoints.addRestaurant)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addRestaurantDtoJson)
                        .with(csrf())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(addRestaurantDto.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.address").value(addRestaurantDto.getAddress())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.openingTime").value(addRestaurantDto.getOpeningTime().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.closingTime").value(addRestaurantDto.getClosingTime().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.intervalBetweenBookings").value(addRestaurantDto.getIntervalBetweenBookings().toString())
        );
    }

    @Test
    public void updateRestaurant_validId_shouldReturnHttpStatus200() throws Exception {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());

        UpdateRestaurantDto updateRestaurantDtoA = RestaurantTestData.getUpdateRestaurantDtoA();
        String updateRestaurantDtoJson = objectMapper.writeValueAsString(updateRestaurantDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put(RestaurantEndpoints.updateRestaurant(restaurantDto.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRestaurantDtoJson)
                        .with(csrf())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void updateRestaurant_validId_shouldReturnUpdatedRestaurantDto() throws Exception {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());
        UpdateRestaurantDto updateRestaurantDtoA = RestaurantTestData.getUpdateRestaurantDtoA();
        String updateRestaurantDtoJson = objectMapper.writeValueAsString(updateRestaurantDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put(RestaurantEndpoints.updateRestaurant(restaurantDto.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRestaurantDtoJson)
                        .with(csrf())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(restaurantDto.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(updateRestaurantDtoA.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.address").value(updateRestaurantDtoA.getAddress())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.openingTime").value(updateRestaurantDtoA.getOpeningTime().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.closingTime").value(updateRestaurantDtoA.getClosingTime().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.intervalBetweenBookings").value(updateRestaurantDtoA.getIntervalBetweenBookings().toString())
        );
    }

    @Test
    public void updateRestaurant_invalidId_shouldReturnHttpStatus404WithErrorMessageInBody() throws Exception {
        Integer invalidId = 1;
        String expectedErrorMessage = Messages.getRestaurantNotFoundMessage(invalidId);
        UpdateRestaurantDto updateRestaurantDtoA = RestaurantTestData.getUpdateRestaurantDtoA();
        String updateRestaurantDto = objectMapper.writeValueAsString(updateRestaurantDtoA);


        mockMvc.perform(
                MockMvcRequestBuilders.put(RestaurantEndpoints.updateRestaurant(invalidId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRestaurantDto)
                        .with(csrf())
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.content().string(expectedErrorMessage)
        );
    }

    @Test
    public void deleteRestaurant_validId_shouldReturnHttpStatus204() throws Exception {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());

        mockMvc.perform(
                MockMvcRequestBuilders.delete(RestaurantEndpoints.deleteRestaurant(restaurantDto.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void deleteRestaurant_invalidId_shouldReturnHttpStatus404WithErrorMessageInBody() throws Exception {
        Integer invalidId = 1;
        String expectedErrorMessage = Messages.getRestaurantNotFoundMessage(invalidId);

        mockMvc.perform(
                MockMvcRequestBuilders.delete(RestaurantEndpoints.deleteRestaurant(invalidId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.content().string(expectedErrorMessage)
        );
    }
}
