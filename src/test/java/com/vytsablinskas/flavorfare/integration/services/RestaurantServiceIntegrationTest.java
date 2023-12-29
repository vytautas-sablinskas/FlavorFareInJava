package com.vytsablinskas.flavorfare.unit.services;

import com.vytsablinskas.flavorfare.services.interfaces.RestaurantService;
import com.vytsablinskas.flavorfare.shared.dtos.AddRestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.RestaurantDto;
import com.vytsablinskas.flavorfare.utils.RestaurantTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RestaurantServiceIntegrationTest {
    private final RestaurantService underTest;

    @Autowired
    public RestaurantServiceIntegrationTest(RestaurantService underTest) {
        this.underTest = underTest;
    }

    @Test
    public void getRestaurants_shouldGetAllRestaurants() {
        AddRestaurantDto restaurantA = RestaurantTestData.getAddRestaurantDtoA();
        underTest.addRestaurant(restaurantA);

        AddRestaurantDto addRestaurantDtoB = RestaurantTestData.getAddRestaurantDtoB();
        underTest.addRestaurant(addRestaurantDtoB);

        List<RestaurantDto> restaurants = underTest.getRestaurants();

        assertThat(restaurants)
                .hasSize(2);
    }

    @Test
    public void addRestaurant_shouldAddRestaurantWithAutoGeneratedId() {
        AddRestaurantDto restaurantA = RestaurantTestData.getAddRestaurantDtoA();

        RestaurantDto createdRestaurant = underTest.addRestaurant(restaurantA);

        assertThat(createdRestaurant.getId()).isGreaterThan(0);
    }
}