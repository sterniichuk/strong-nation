package online.strongnation.integration;

import online.strongnation.config.EntityNameLength;
import online.strongnation.model.dto.CategoryDTO;
import online.strongnation.model.entity.Country;
import online.strongnation.model.entity.Region;
import online.strongnation.exception.CountryNotFoundException;
import online.strongnation.exception.IllegalRegionException;
import online.strongnation.exception.RegionNotFoundException;
import online.strongnation.repository.CountryRepository;
import online.strongnation.repository.RegionRepository;
import online.strongnation.service.RegionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class RegionServiceImplTest {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private RegionService service;

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

    private final String FOOD = "food";
    private final Float NUMBER_OF_FOOD = 10.101f;
    private final String FOOD_UNITS = "kg";
    private final String BIG_FOOD_UNITS = "ton";

    private final String CARS = "car";
    private final Float NUMBER_OF_CARS = 50F;

    private final CategoryDTO WASHINGTON_IN_USA_FOOD_CATEGORY = CategoryDTO.builder()
            .name(FOOD)
            .number(NUMBER_OF_FOOD)
            .units(FOOD_UNITS)
            .build();
    private final CategoryDTO WARSAW_IN_USA_FOOD_CATEGORY = CategoryDTO.builder()
            .name(FOOD)
            .number(NUMBER_OF_FOOD * 1.5f)
            .units(FOOD_UNITS)
            .build();
    private final CategoryDTO USA_FOOD_CATEGORY = CategoryDTO.builder()
            .name(FOOD)
            .number(WARSAW_IN_USA_FOOD_CATEGORY.getNumber() + WASHINGTON_IN_USA_FOOD_CATEGORY.getNumber())
            .units(FOOD_UNITS)
            .build();
    private final CategoryDTO WASHINGTON_IN_POLAND_FOOD_CATEGORY = CategoryDTO.builder()
            .name(FOOD)
            .number(NUMBER_OF_FOOD * 1.5f)
            .units(FOOD_UNITS)
            .build();
    private final CategoryDTO WARSAW_IN_POLAND_FOOD_CATEGORY = CategoryDTO.builder()
            .name(FOOD)
            .number(NUMBER_OF_FOOD * 1.1f)
            .units(FOOD_UNITS)
            .build();
    private final CategoryDTO POLAND_FOOD_CATEGORY = CategoryDTO.builder()
            .name(FOOD)
            .number(WARSAW_IN_POLAND_FOOD_CATEGORY.getNumber() + WASHINGTON_IN_POLAND_FOOD_CATEGORY.getNumber())
            .units(FOOD_UNITS)
            .build();

    private final CategoryDTO WASHINGTON_IN_USA_CARS_CATEGORY = CategoryDTO.builder()
            .name(CARS)
            .number(NUMBER_OF_CARS)
            .build();
    private final CategoryDTO WARSAW_IN_USA_CARS_CATEGORY = CategoryDTO.builder()
            .name(CARS)
            .number(NUMBER_OF_CARS * 2f)
            .build();
    private final CategoryDTO USA_CARS_CATEGORY = CategoryDTO.builder()
            .name(CARS)
            .number(WARSAW_IN_USA_CARS_CATEGORY.getNumber() + WASHINGTON_IN_USA_CARS_CATEGORY.getNumber())
            .build();
    private final CategoryDTO WASHINGTON_IN_POLAND_CARS_CATEGORY = CategoryDTO.builder()
            .name(CARS)
            .number(NUMBER_OF_CARS * 3f)
            .build();
    private final CategoryDTO WARSAW_IN_POLAND_CARS_CATEGORY = CategoryDTO.builder()
            .name(CARS)
            .number(NUMBER_OF_CARS * 4f)
            .build();
    private final CategoryDTO POLAND_CARS_CATEGORY = CategoryDTO.builder()
            .name(CARS)
            .number(WARSAW_IN_POLAND_CARS_CATEGORY.getNumber() + WASHINGTON_IN_POLAND_CARS_CATEGORY.getNumber())
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

    //write tests for categories
    @Autowired
    private RegionRepository regionRepository;

    @BeforeEach
    void setUp() {
        //usa
        Region washington = new Region(WASHINGTON_NAME);
        washington.setMoney(WASHINGTON_IN_USA_MONEY);
        washington.setCategoriesDTO(CATEGORIES_OF_WASHINGTON_OF_USA);
        Region warsawInUSA = new Region(WARSAW_NAME);
        warsawInUSA.setMoney(WARSAW_IN_USA_MONEY);
        warsawInUSA.setCategoriesDTO(CATEGORIES_OF_WARSAW_OF_USA);
        Country USACountry = new Country(USA_NAME);
        USACountry.setMoney(USA_MONEY);
        USACountry.setCategoriesDTO(CATEGORIES_OF_USA);
        USACountry.setRegions(List.of(washington, warsawInUSA));
        //poland
        Country polandCountry = new Country(POLAND_NAME);
        Region washingtonInPoland = new Region(WASHINGTON_NAME);
        washingtonInPoland.setCategoriesDTO(CATEGORIES_OF_WASHINGTON_OF_POLAND);
        washingtonInPoland.setMoney(WASHINGTON_IN_POLAND_MONEY);
        Region warsawInPoland = new Region(WARSAW_NAME);
        warsawInPoland.setMoney(WARSAW_IN_POLAND_MONEY);
        warsawInPoland.setCategoriesDTO(CATEGORIES_OF_WARSAW_OF_POLAND);
        polandCountry.setRegions(List.of(washingtonInPoland, warsawInPoland));
        polandCountry.setMoney(POLAND_MONEY);
        polandCountry.setCategoriesDTO(CATEGORIES_OF_POLAND);
        countryRepository.saveAll(List.of(polandCountry, USACountry));
    }

    @AfterEach
    void tearDown() {
        countryRepository.deleteAll();
        regionRepository.deleteAll();
    }

    @Test
    void createSimple() {
        //given
        final String newRegionName = "New Region Name";
        //when
        service.create(USA_NAME, newRegionName);
        //then
        assertThat(regionRepository.existsRegionByName(newRegionName)).isTrue();
    }

    @Test
    void createAndCutExcessiveWhiteSpaces() {
        //given
        final String newRegionName = "      \nNew   \t\t\t\t                               Region       Name       ";
        final String expectedRegionName = "New Region Name";
        //when
        service.create(USA_NAME, newRegionName);
        //then
        assertThat(regionRepository.existsRegionByName(expectedRegionName)).isTrue();
        assertThat(regionRepository.existsRegionByName(newRegionName)).isFalse();
    }

    @Test
    void createToSpecificCountry() {
        //given
        final String regionName = "New Region Name";
        //when
        service.create(USA_NAME, regionName);
        //then
        assertThat(regionRepository.existsRegionInCountryByNamesIgnoringCase(USA_NAME, regionName)).isTrue();
        assertThat(regionRepository.existsRegionInCountryByNamesIgnoringCase(POLAND_NAME, regionName)).isFalse();
    }

    @Test
    void createWhenCountryDoesntExist() {
        //given
        final String regionName = "New Region Name";
        //when
        //then
        assertThatThrownBy(() -> service.create("Russia", regionName))
                .isInstanceOf(CountryNotFoundException.class);
    }

    @Test
    void createWhenRegionAlreadyExistsLowerCase() {
        //given
        final String regionName = "New Region Name";
        Country country = countryRepository.findCountryByName(USA_NAME)
                .orElseThrow(CountryNotFoundException::new);
        country.setRegions(List.of(new Region(regionName)));
        countryRepository.save(country);
        //when
        //then
        assertThatThrownBy(() -> service.create(USA_NAME, regionName.toLowerCase()))
                .isInstanceOf(IllegalRegionException.class);
    }

    @Test
    void createWhenRegionAlreadyExistsUpperCase() {
        //given
        final String regionName = "New Region Name";
        Country country = countryRepository.findCountryByName(USA_NAME)
                .orElseThrow(CountryNotFoundException::new);
        country.setRegions(List.of(new Region(regionName)));
        countryRepository.save(country);
        //when
        //then
        assertThatThrownBy(() -> service.create(USA_NAME, regionName.toUpperCase()))
                .isInstanceOf(IllegalRegionException.class);
    }

    @Test
    void createAll() {
        //given
        final List<String> strings = List.of("Some region 1",
                "Some region 2",
                "Some region 3",
                "Some region 4");
        //when
        service.createAll(USA_NAME, strings);
        //then
        for (var i : strings) {
            assertThat(regionRepository
                    .existsRegionInCountryByNamesIgnoringCase(USA_NAME, i)).isTrue();
        }
    }

    @Test
    void createAllWhenRegionAlreadyExists() {
        //given
        final String regionName = "New Region Name";
        final List<String> strings = List.of("Some region 1",
                "Some region 2",
                "Some region 3",
                regionName,
                "Some region 4");
        Country country = countryRepository.findCountryByName(USA_NAME)
                .orElseThrow(CountryNotFoundException::new);
        country.setRegions(List.of(new Region(regionName)));
        countryRepository.save(country);
        //when
        //then
        assertThatThrownBy(() -> service.createAll(USA_NAME, strings))
                .isInstanceOf(IllegalRegionException.class)
                .hasMessage("Region " + regionName + " already exists. List of regions is not saved");
        for (var i : strings) {
            assertThat(regionRepository
                    .existsRegionInCountryByNamesIgnoringCase(USA_NAME, i)).isFalse();
        }
    }

    @Test
    void rename() {
        //given
        final String newName = "Some new name";
        final var expected = regionRepository
                .findRegionDTOInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new);
        expected.setName(newName);
        //when
        var actual = service.rename(USA_NAME, WASHINGTON_NAME, newName);
        //then
        assertThat(actual).isEqualTo(expected);
        assertThat(regionRepository
                .existsRegionInCountryByNamesIgnoringCase(POLAND_NAME, WASHINGTON_NAME))
                .isTrue();
        assertThat(regionRepository
                .existsRegionInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME))
                .isFalse();
    }

    @Test
    void renameWithBlankSymbols() {
        //given
        final String expectedName = "Some new name";
        final String givenNewName = "        Some  \n\t new \t  name    \n";
        final var expected = regionRepository
                .findRegionDTOInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new);
        expected.setName(expectedName);
        //when
        var actual = service.rename(USA_NAME, WASHINGTON_NAME, givenNewName);
        //then
        assertThat(actual).isEqualTo(expected);
        assertThat(regionRepository
                .existsRegionInCountryByNamesIgnoringCase(POLAND_NAME, WASHINGTON_NAME))
                .isTrue();
        assertThat(regionRepository
                .existsRegionInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME))
                .isFalse();
        assertThat(regionRepository
                .existsRegionInCountryByNamesIgnoringCase(USA_NAME, givenNewName))
                .isFalse();
        assertThat(regionRepository
                .existsRegionInCountryByNamesIgnoringCase(USA_NAME, expectedName))
                .isTrue();
    }

    @Test
    void renameWithTooLongRegionOld() {
        //given
        byte[] array = new byte[EntityNameLength.REGION.length + 1];
        new Random().nextBytes(array);
        final var input = new String(array, StandardCharsets.UTF_8);
        //when
        //then
        assertThatThrownBy(() -> service.rename(USA_NAME, input, WASHINGTON_NAME))
                .isInstanceOf(RegionNotFoundException.class);
    }

    @Test
    void renameWithTooLongCountryNew() {
        //given
        byte[] array = new byte[EntityNameLength.REGION.length + 1];
        new Random().nextBytes(array);
        final var input = new String(array, StandardCharsets.UTF_8);
        //when
        //then
        assertThatThrownBy(() -> service.rename(USA_NAME, WASHINGTON_NAME, input))
                .isInstanceOf(RegionNotFoundException.class);
    }

    @Test
    void renameById() {
        //given
        final String expectedName = "Some new name";
        final String givenNewName = "        Some  \n\t new \t  name    \n";
        final var expected = regionRepository
                .findRegionDTOInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new);
        expected.setName(expectedName);
        //when
        var actual = service.rename(expected.getId(), givenNewName);
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteCheckMoneyOfCountry() {
        //given
        final var expected = regionRepository
                .findRegionDTOInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new);
        //when
        var actual = service.delete(USA_NAME, WASHINGTON_NAME);
        //then
        assertThat(actual).isEqualTo(expected);
        assertThat(countryRepository
                .findCountryDTOByNameIgnoreCase(POLAND_NAME)
                .orElseThrow(CountryNotFoundException::new)
                .getMoney())
                .isEqualTo(POLAND_MONEY);
        assertThat(countryRepository
                .findCountryDTOByNameIgnoreCase(USA_NAME)
                .orElseThrow(CountryNotFoundException::new)
                .getMoney())
                .isEqualTo(WARSAW_IN_USA_MONEY);
    }

    @Test
    void deleteCheckMoneyOfRegions() {
        //given
        final var expected = regionRepository
                .findRegionDTOInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new);
        //when
        var actual = service.delete(USA_NAME, WASHINGTON_NAME);
        //then
        assertThat(actual).isEqualTo(expected);
        assertThat(regionRepository
                .findRegionDTOInCountryByNamesIgnoringCase(USA_NAME, WARSAW_NAME)
                .orElseThrow(RegionNotFoundException::new)
                .getMoney())
                .isEqualTo(WARSAW_IN_USA_MONEY);
        assertThat(regionRepository
                .findRegionDTOInCountryByNamesIgnoringCase(POLAND_NAME, WARSAW_NAME)
                .orElseThrow(RegionNotFoundException::new)
                .getMoney())
                .isEqualTo(WARSAW_IN_POLAND_MONEY);
        assertThat(regionRepository
                .findRegionDTOInCountryByNamesIgnoringCase(POLAND_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new)
                .getMoney())
                .isEqualTo(WASHINGTON_IN_POLAND_MONEY);
    }

    @Test
    void deleteById() {
        //given
        final var expected = regionRepository
                .findRegionDTOInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new);
        //when
        var actual = service.delete(expected.getId());
        //then
        assertThat(actual).isEqualTo(expected);
        assertThat(regionRepository
                .existsRegionInCountryByNamesIgnoringCase(POLAND_NAME, WASHINGTON_NAME))
                .isTrue();
        assertThat(regionRepository
                .existsRegionInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME))
                .isFalse();
    }

    @Test
    void deleteAll() {
        //when
        service.deleteAllByCountry(USA_NAME);
        //then
        boolean emptyRegionsUSA = regionRepository.findAllRegionDTOByCountryNameIgnoringCase(USA_NAME).isEmpty();
        assertThat(emptyRegionsUSA).isTrue();
        boolean emptyRegionsPoland = regionRepository.findAllRegionDTOByCountryNameIgnoringCase(POLAND_NAME).isEmpty();
        assertThat(emptyRegionsPoland).isFalse();
        var zero = countryRepository.findCountryByName(USA_NAME)
                .orElseThrow(CountryNotFoundException::new).getName();
        assertThat(zero).isEqualTo(BigDecimal.ZERO);
        var notZero = countryRepository.findCountryByName(POLAND_NAME)
                .orElseThrow(CountryNotFoundException::new).getName();
        assertThat(notZero).isNotEqualTo(BigDecimal.ZERO);
    }
}