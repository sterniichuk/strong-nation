package online.strongnation.unit.service;

import online.strongnation.config.Floats;
import online.strongnation.model.dto.CategoryDTO;
import online.strongnation.model.dto.CountryDTO;
import online.strongnation.model.dto.RegionDTO;
import online.strongnation.model.statistic.StatisticResult;
import online.strongnation.service.StatisticService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class StatisticServiceTest {

    @Autowired
    private StatisticService service;

    private final String FOOD = "food";
    private final BigDecimal NUMBER_OF_FOOD = BigDecimal.valueOf(10.10);
    private final String FOOD_UNITS = "kg";
    private final String BIG_FOOD_UNITS = "ton";

    private final String CARS = "car";
    private final BigDecimal NUMBER_OF_CARS = BigDecimal.valueOf(50);

    private final String USA_NAME = "United States of America";
    private final String POLAND_NAME = "Poland";
    private final String WASHINGTON_NAME = "Washington D.C.";
    private final String WARSAW_NAME = "Warsaw";

    private final BigDecimal WASHINGTON_IN_USA_MONEY = new BigDecimal("10101010.11");
    private final BigDecimal WARSAW_IN_USA_MONEY = new BigDecimal("2022.21");
    private final BigDecimal USA_MONEY = WASHINGTON_IN_USA_MONEY.add(WARSAW_IN_USA_MONEY);
    private final BigDecimal WASHINGTON_IN_POLAND_MONEY = new BigDecimal("1010.33");
    private final BigDecimal WARSAW_IN_POLAND_MONEY = new BigDecimal("1010101010.22");
    private final BigDecimal POLAND_MONEY = WASHINGTON_IN_POLAND_MONEY.add(WARSAW_IN_POLAND_MONEY);

    private final CategoryDTO WASHINGTON_IN_USA_FOOD_CATEGORY = CategoryDTO.builder()
            .name(FOOD)
            .number(NUMBER_OF_FOOD)
            .units(FOOD_UNITS)
            .build();
    private final CategoryDTO WARSAW_IN_USA_FOOD_CATEGORY = CategoryDTO.builder()
            .name(FOOD)
            .number(NUMBER_OF_FOOD.add(BigDecimal.valueOf(1.5f)))
            .units(FOOD_UNITS)
            .build();
    private final CategoryDTO USA_FOOD_CATEGORY = CategoryDTO.builder()
            .name(FOOD)
            .number(WARSAW_IN_USA_FOOD_CATEGORY.getNumber().add(WASHINGTON_IN_USA_FOOD_CATEGORY.getNumber()))
            .units(FOOD_UNITS)
            .build();
    private final CategoryDTO WASHINGTON_IN_POLAND_FOOD_CATEGORY = CategoryDTO.builder()
            .name(FOOD)
            .number(NUMBER_OF_FOOD.add(BigDecimal.valueOf(1.5f)))
            .units(FOOD_UNITS)
            .build();
    private final CategoryDTO WARSAW_IN_POLAND_FOOD_CATEGORY = CategoryDTO.builder()
            .name(FOOD)
            .number(NUMBER_OF_FOOD.add(BigDecimal.valueOf(1.1f)))
            .units(FOOD_UNITS)
            .build();
    private final CategoryDTO POLAND_FOOD_CATEGORY = CategoryDTO.builder()
            .name(FOOD)
            .number(WARSAW_IN_POLAND_FOOD_CATEGORY.getNumber().add(WASHINGTON_IN_POLAND_FOOD_CATEGORY.getNumber()))
            .units(FOOD_UNITS)
            .build();

    private final CategoryDTO WASHINGTON_IN_USA_CARS_CATEGORY = CategoryDTO.builder()
            .name(CARS)
            .number(NUMBER_OF_CARS)
            .build();
    private final CategoryDTO WARSAW_IN_USA_CARS_CATEGORY = CategoryDTO.builder()
            .name(CARS)
            .number(NUMBER_OF_CARS.add(BigDecimal.valueOf(2f)))
            .build();
    private final CategoryDTO USA_CARS_CATEGORY = CategoryDTO.builder()
            .name(CARS)
            .number(WARSAW_IN_USA_CARS_CATEGORY.getNumber().add(WASHINGTON_IN_USA_CARS_CATEGORY.getNumber()))
            .build();
    private final CategoryDTO WASHINGTON_IN_POLAND_CARS_CATEGORY = CategoryDTO.builder()
            .name(CARS)
            .number(NUMBER_OF_CARS.add(BigDecimal.valueOf(3f)))
            .build();
    private final CategoryDTO WARSAW_IN_POLAND_CARS_CATEGORY = CategoryDTO.builder()
            .name(CARS)
            .number(NUMBER_OF_CARS.add(BigDecimal.valueOf(4f)))
            .build();
    private final CategoryDTO POLAND_CARS_CATEGORY = CategoryDTO.builder()
            .name(CARS)
            .number(WARSAW_IN_POLAND_CARS_CATEGORY.getNumber().add(WASHINGTON_IN_POLAND_CARS_CATEGORY.getNumber()))
            .build();

    private final List<CategoryDTO> CATEGORIES_OF_WASHINGTON_OF_USA =
            List.of(WASHINGTON_IN_USA_CARS_CATEGORY, WASHINGTON_IN_USA_FOOD_CATEGORY);
    private final List<CategoryDTO> CATEGORIES_OF_WARSAW_OF_USA =
            List.of(WARSAW_IN_USA_CARS_CATEGORY, WARSAW_IN_USA_FOOD_CATEGORY);
    private final List<CategoryDTO> CATEGORIES_OF_WASHINGTON_OF_POLAND =
            List.of(WASHINGTON_IN_POLAND_CARS_CATEGORY, WASHINGTON_IN_POLAND_FOOD_CATEGORY);
    private final List<CategoryDTO> CATEGORIES_OF_WARSAW_OF_POLAND =
            List.of(WARSAW_IN_POLAND_CARS_CATEGORY, WARSAW_IN_POLAND_FOOD_CATEGORY);

    private final List<CategoryDTO> CATEGORIES_OF_POLAND =
            List.of(POLAND_CARS_CATEGORY, POLAND_FOOD_CATEGORY);
    private final List<CategoryDTO> CATEGORIES_OF_USA =
            List.of(USA_CARS_CATEGORY, USA_FOOD_CATEGORY);

    //usa
    private final RegionDTO WASHINGTON_IN_USA = RegionDTO.builder()
            .money(WASHINGTON_IN_USA_MONEY)
            .categories(CATEGORIES_OF_WASHINGTON_OF_USA)
            .name(WASHINGTON_NAME)
            .build();
    private final RegionDTO WARSAW_IN_USA = RegionDTO.builder()
            .money(WARSAW_IN_USA_MONEY)
            .categories(CATEGORIES_OF_WARSAW_OF_USA)
            .name(WARSAW_NAME)
            .build();
    private final CountryDTO USA = CountryDTO.builder()
            .money(USA_MONEY)
            .categories(CATEGORIES_OF_USA)
            .name(USA_NAME)
            .build();
    //poland
    private final RegionDTO WASHINGTON_IN_POLAND = RegionDTO.builder()
            .money(WASHINGTON_IN_POLAND_MONEY)
            .categories(CATEGORIES_OF_WASHINGTON_OF_POLAND)
            .name(WASHINGTON_NAME)
            .build();
    private final RegionDTO WARSAW_IN_POLAND = RegionDTO.builder()
            .money(WARSAW_IN_POLAND_MONEY)
            .categories(CATEGORIES_OF_WARSAW_OF_POLAND)
            .name(WARSAW_NAME)
            .build();
    private final CountryDTO POLAND = CountryDTO.builder()
            .money(POLAND_MONEY)
            .categories(CATEGORIES_OF_POLAND)
            .name(POLAND_NAME)
            .build();

    @Test
    void deleteTestUpdatedCategories() {
        //given
        //when
        StatisticResult statisticResult = service.deleteChild(USA, WASHINGTON_IN_USA);
        var actual = statisticResult.updatedCategories();
        //then
        assertThat(statisticResult.newMoneyValue().orElseThrow(IllegalStateException::new)
                .compareTo(WARSAW_IN_USA_MONEY)).isEqualTo(0);
        assertThat(actual).isEqualTo(CATEGORIES_OF_WARSAW_OF_USA);
        assertThat(statisticResult.excessiveCategories().isEmpty()).isTrue();
        assertThat(statisticResult.newCategories().isEmpty()).isTrue();
    }

    @Test
    void deleteTestExcessiveCategories() {
        //given
        CategoryDTO someRareCategory = CategoryDTO.builder()
                .name("some name")
                .number(NUMBER_OF_FOOD.multiply(BigDecimal.valueOf(1.5f)))
                .units(FOOD_UNITS)
                .build();
        CountryDTO testCountry = CountryDTO.builder()
                .money(USA_MONEY.add(POLAND_MONEY))
                .categories(List.of(USA_FOOD_CATEGORY, someRareCategory)).build();
        RegionDTO testRegion = RegionDTO.builder()
                .money(USA_MONEY)
                .categories(List.of(someRareCategory)).build();
        //when
        StatisticResult statisticResult = service.deleteChild(testCountry, testRegion);
        var actual = statisticResult.excessiveCategories();
        //then
        assertThat(statisticResult.newMoneyValue().orElseThrow(IllegalStateException::new)
                .compareTo(POLAND_MONEY)).isEqualTo(0);
        assertThat(actual).isEqualTo(someRareCategory);
        assertThat(statisticResult.updatedCategories().isEmpty()).isTrue();
        assertThat(statisticResult.newCategories().isEmpty()).isTrue();
    }

    @Test
    void updateNothing() {
        //when
        StatisticResult actual = service.updateChild(USA, WASHINGTON_IN_USA, WASHINGTON_IN_USA);
        //then
        assertThat(actual.newMoneyValue()).isEmpty();
        assertThat(actual.excessiveCategories().isEmpty()).isTrue();
        assertThat(actual.updatedCategories().isEmpty()).isTrue();
        assertThat(actual.newCategories().isEmpty()).isTrue();
    }

    @Test
    void update() {
        //given
        BigDecimal newMoney = WASHINGTON_IN_USA_MONEY.divide(
                BigDecimal.valueOf(2), Floats.MONEY_SCALE, Floats.MONEY_ROUNDING);
        BigDecimal newFoodNumber = NUMBER_OF_FOOD.divide(
                BigDecimal.valueOf(2), Floats.MONEY_SCALE, Floats.MONEY_ROUNDING);
        final var newFoodCategory = WASHINGTON_IN_USA_FOOD_CATEGORY.toBuilder()
                .number(newFoodNumber).build();
        final var newWashington = RegionDTO.builder()
                .money(newMoney)
                .categories(List.of(newFoodCategory, WASHINGTON_IN_USA_CARS_CATEGORY)).build();
        //when
        StatisticResult actual = service.updateChild(USA, WASHINGTON_IN_USA, newWashington);
        //then
        assertThat(actual.newMoneyValue().orElseThrow(IllegalStateException::new))
                .isEqualTo(USA_MONEY.subtract(newMoney));
        assertThat(actual.excessiveCategories().isEmpty()).isTrue();
        assertThat(actual.updatedCategories().get(0))
                .isEqualTo(USA_FOOD_CATEGORY.toBuilder()
                        .number(USA_FOOD_CATEGORY.getNumber().subtract(newFoodNumber)));
        assertThat(actual.newCategories().isEmpty()).isTrue();
    }

    @Test
    void addChildWithNewCategory() {
        //given
        CategoryDTO someRareCategory = CategoryDTO.builder()
                .name("some name")
                .number(NUMBER_OF_FOOD.multiply(BigDecimal.valueOf(1.5f)))
                .units(FOOD_UNITS)
                .build();
        RegionDTO testRegion = RegionDTO.builder()
                .money(USA_MONEY)
                .categories(List.of(someRareCategory)).build();
        //when
        final var actual = service.addChildToParent(USA, testRegion);
        //then
        assertThat(actual.newMoneyValue().orElseThrow(IllegalStateException::new))
                .isEqualTo(USA_MONEY.multiply(BigDecimal.valueOf(2)));
        assertThat(actual.excessiveCategories().isEmpty()).isTrue();
        assertThat(actual.newCategories().get(0))
                .isEqualTo(someRareCategory);
        assertThat(actual.updatedCategories().isEmpty()).isTrue();
    }

    @Test
    void addChildWithExistedCategory() {
        //given
        RegionDTO testRegion = RegionDTO.builder()
                .money(USA_MONEY)
                .categories(List.of(USA_FOOD_CATEGORY)).build();
        //when
        final var actual = service.addChildToParent(USA, testRegion);
        //then
        assertThat(actual.newMoneyValue().orElseThrow(IllegalStateException::new))
                .isEqualTo(USA_MONEY.multiply(BigDecimal.valueOf(2)));
        assertThat(actual.excessiveCategories().isEmpty()).isTrue();
        assertThat(actual.updatedCategories().get(0))
                .isEqualTo(CategoryDTO.builder()
                        .number(USA_FOOD_CATEGORY.getNumber().multiply(BigDecimal.valueOf(2)))
                        .name(FOOD)
                        .units(FOOD_UNITS).build()
                );
        assertThat(actual.newCategories().isEmpty()).isTrue();
    }
}