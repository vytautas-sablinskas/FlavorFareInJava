package com.vytsablinskas.flavorfare.integration.services;

import com.vytsablinskas.flavorfare.business.exceptions.ResourceNotFoundException;
import com.vytsablinskas.flavorfare.business.services.interfaces.RestaurantService;
import com.vytsablinskas.flavorfare.business.services.interfaces.TableService;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.RestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.AddTableDto;
import com.vytsablinskas.flavorfare.shared.dtos.table.TableDto;
import com.vytsablinskas.flavorfare.utils.RestaurantTestData;
import com.vytsablinskas.flavorfare.utils.TableTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TableServiceIntegrationTests {

    private final RestaurantService restaurantService;
    private final TableService underTest;

    @Autowired
    public TableServiceIntegrationTests(TableService tableService, RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
        this.underTest = tableService;
    }

    @Test
    public void addTable_validRestaurantId_shouldAddTableAndReturnTableDtoWithAutoGeneratedId() {
        Integer restaurantId = addRestaurantForTesting();
        AddTableDto addTableDtoA = TableTestData.getAddTableDtoA();

        TableDto tableDto = underTest.addTable(restaurantId, addTableDtoA);

        assertThat(tableDto.getCount()).isEqualTo(addTableDtoA.getCount());
        assertThat(tableDto.getSize()).isEqualTo(addTableDtoA.getSize());
        assertThat(tableDto.getRestaurantId()).isEqualTo(restaurantId);
    }

    @Test
    public void addTable_invalidRestaurantId_shouldThrowResourceNotFoundException() {
        assertThatThrownBy(() ->
                underTest.addTable(1, TableTestData.getAddTableDtoA())
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    private Integer addRestaurantForTesting() {
        RestaurantDto restaurantDto = restaurantService.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());

        return restaurantDto.getId();
    }
}
