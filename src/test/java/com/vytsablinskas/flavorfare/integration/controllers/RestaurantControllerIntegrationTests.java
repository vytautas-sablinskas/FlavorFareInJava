package com.vytsablinskas.flavorfare.integration.controllers;

import com.vytsablinskas.flavorfare.business.services.interfaces.RestaurantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class RestaurantControllerIntegrationTests {
    private MockMvc mockMvc;
    private RestaurantService restaurantService;

    @Autowired
    public RestaurantControllerIntegrationTests(MockMvc mockMvc, RestaurantService restaurantService) {
        this.mockMvc = mockMvc;
        this.restaurantService = restaurantService;
    }

    @Test
    public void getRestaurants_shouldReturnHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
