package online.strongnation.unit.service;

import online.strongnation.business.model.dto.CategoryDTO;
import online.strongnation.business.model.entity.Country;
import online.strongnation.business.model.statistic.CategoryHolder;
import online.strongnation.business.model.statistic.StatisticResult;
import online.strongnation.business.service.StatisticOfEntityUpdater;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class StatisticOfEntityUpdaterTest {

    @Autowired
    private StatisticOfEntityUpdater service;

    private final String FOOD = "food";
    private final BigDecimal NUMBER_OF_FOOD = BigDecimal.valueOf(10.10);
    private final String FOOD_UNITS = "kg";

    private final String CARS = "car";
    private final BigDecimal NUMBER_OF_CARS = BigDecimal.valueOf(50);

    private final String WATER = "water";
    private final BigDecimal NUMBER_OF_WATER = BigDecimal.valueOf(50.25);
    private final String WATER_UNITS = "liter";

    private final CategoryDTO FOOD_CATEGORY = CategoryDTO.builder()
            .name(FOOD)
            .number(NUMBER_OF_FOOD)
            .units(FOOD_UNITS)
            .build();

    private final CategoryDTO CARS_CATEGORY = CategoryDTO.builder()
            .name(CARS)
            .number(NUMBER_OF_CARS)
            .build();

    private final CategoryDTO UPDATED_CARS_CATEGORY = CategoryDTO.builder()
            .name(CARS)
            .number(NUMBER_OF_CARS.multiply(BigDecimal.valueOf(2)))
            .build();

    private final CategoryDTO WATER_CATEGORY = CategoryDTO.builder()
            .name(WATER)
            .number(NUMBER_OF_WATER)
            .units(WATER_UNITS)
            .build();

    @Test
    void update() {
        //given
        Country country = new Country("usa");
        country.setCategoriesDTO(List.of(FOOD_CATEGORY, CARS_CATEGORY));
        StatisticResult statistic = StatisticResult.builder().newCategories(List.of(WATER_CATEGORY))
                .excessiveCategories(List.of(FOOD_CATEGORY))
                .updatedCategories(List.of(UPDATED_CARS_CATEGORY)).build();
        //when
        service.update(country, statistic);
        //then
        var categories = country.getCategories().stream().map(CategoryHolder::getCategoryDAO)
                .map(CategoryDTO::new).toList();
        assertThat(categories.contains(UPDATED_CARS_CATEGORY)).isTrue();
        assertThat(categories.contains(CARS_CATEGORY)).isFalse();
        assertThat(categories.contains(WATER_CATEGORY)).isTrue();
        assertThat(categories.contains(FOOD_CATEGORY)).isFalse();
    }
}