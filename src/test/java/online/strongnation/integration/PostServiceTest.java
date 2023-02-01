package online.strongnation.integration;

import online.strongnation.config.Floats;
import online.strongnation.exception.CountryNotFoundException;
import online.strongnation.exception.RegionNotFoundException;
import online.strongnation.model.dto.CategoryDTO;
import online.strongnation.model.dto.CountryDTO;
import online.strongnation.model.dto.PostDTO;
import online.strongnation.model.dto.RegionDTO;
import online.strongnation.model.entity.Country;
import online.strongnation.model.entity.Post;
import online.strongnation.repository.CountryRepository;
import online.strongnation.repository.PostRepository;
import online.strongnation.repository.RegionRepository;
import online.strongnation.service.PostService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static online.strongnation.service.implementation.CategoryUtils.getCategoryMap;
import static online.strongnation.service.implementation.RequestParameterFixer.checkDate;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private CountryRepository countryRepository;

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

    @Autowired
    private PostService service;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        final Country USA_ENTITY = new Country(USA);
        USA_ENTITY.setRegionsDTO(List.of(WASHINGTON_IN_USA, WARSAW_IN_POLAND));
        countryRepository.save(USA_ENTITY);
    }

    @AfterEach
    void tearDown() {
        countryRepository.deleteAll();
    }

    @Test
    void createTestMoneyByNames() {
        //given
        final String heading = "first post some heading";
        final BigDecimal money = BigDecimal.valueOf(1020.22);
        final String link = "localH0sT";
        final PostDTO post = PostDTO.builder()
                .money(money)
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .heading(heading)
                .build();
        //when
        PostDTO actual = service.create(post, USA_NAME, WASHINGTON_NAME);
        //then
        assertThat(actual.getId()).isNotNull();
        var country = countryRepository.findCountryByName(USA_NAME).orElseThrow(CountryNotFoundException::new);
        var region = regionRepository.findRegionDTOInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(CountryNotFoundException::new);
        assertThat(country.getMoney().compareTo(USA_MONEY.add(money))).isEqualTo(0);
        assertThat(region.getMoney().compareTo(WASHINGTON_IN_USA_MONEY.add(money))).isEqualTo(0);
    }

    @Test
    void createTestCategoriesByNames() {
        //given
        final String heading = "first post some heading";
        final BigDecimal money = BigDecimal.valueOf(1020.22);
        final String link = "localH0sT";
        final PostDTO post = PostDTO.builder()
                .money(money)
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .heading(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY))
                .build();
        //when
        PostDTO actual = service.create(post, USA_NAME, WASHINGTON_NAME);
        //then
        assertThat(actual.getId()).isNotNull();
        var country = countryRepository.findCountryDTOByNameIgnoreCase(USA_NAME).orElseThrow(CountryNotFoundException::new);
        var region = regionRepository.findRegionDTOInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(CountryNotFoundException::new);
        var regionCategoryMap = getCategoryMap(region.getCategories());
        var countryCategoryMap = getCategoryMap(country.getCategories());
        assertThat(regionCategoryMap.get(FOOD).getNumber()
                .compareTo(WASHINGTON_IN_USA_FOOD_CATEGORY.getNumber().multiply(BigDecimal.valueOf(2))))
                .isEqualTo(0);
        assertThat(countryCategoryMap.get(FOOD).getNumber()
                .compareTo(USA_FOOD_CATEGORY.getNumber().add(WASHINGTON_IN_USA_FOOD_CATEGORY.getNumber())))
                .isEqualTo(0);
    }

    @Test
    void createTestMoneyById() {
        //given
        final String heading = "first post some heading";
        final BigDecimal money = BigDecimal.valueOf(1020.22);
        final String link = "localH0sT";
        final PostDTO post = PostDTO.builder()
                .money(money)
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .heading(heading)
                .build();
        Long regionId = regionRepository
                .findRegionDTOInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new)
                .getId();
        //when
        PostDTO actual = service.create(post, regionId);
        //then
        assertThat(actual.getId()).isNotNull();
        var country = countryRepository.findCountryByName(USA_NAME).orElseThrow(CountryNotFoundException::new);
        var region = regionRepository.findRegionDTOInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(CountryNotFoundException::new);
        assertThat(country.getMoney().compareTo(USA_MONEY.add(money))).isEqualTo(0);
        assertThat(region.getMoney().compareTo(WASHINGTON_IN_USA_MONEY.add(money))).isEqualTo(0);
    }

    @Test
    void createTestCategoriesById() {
        //given
        final String heading = "first post some heading";
        final BigDecimal money = BigDecimal.valueOf(1020.22);
        final String link = "localH0sT";
        final PostDTO post = PostDTO.builder()
                .money(money)
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .heading(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY))
                .build();
        Long regionId = regionRepository
                .findRegionDTOInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new)
                .getId();
        //when
        PostDTO actual = service.create(post, regionId);
        //then
        assertThat(actual.getId()).isNotNull();
        var country = countryRepository.findCountryDTOByNameIgnoreCase(USA_NAME).orElseThrow(CountryNotFoundException::new);
        var region = regionRepository.findRegionDTOInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(CountryNotFoundException::new);
        var regionCategoryMap = getCategoryMap(region.getCategories());
        var countryCategoryMap = getCategoryMap(country.getCategories());
        assertThat(regionCategoryMap.get(FOOD).getNumber()
                .compareTo(WASHINGTON_IN_USA_FOOD_CATEGORY.getNumber().multiply(BigDecimal.valueOf(2))))
                .isEqualTo(0);
        assertThat(countryCategoryMap.get(FOOD).getNumber()
                .compareTo(USA_FOOD_CATEGORY.getNumber().add(WASHINGTON_IN_USA_FOOD_CATEGORY.getNumber())))
                .isEqualTo(0);
    }

    @Test
    void all() {
        //given
        final String heading = "first post some heading";
        final BigDecimal money = BigDecimal.valueOf(1020.22);
        final String link = "localH0sT";
        final PostDTO post = PostDTO.builder()
                .money(money)
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .heading(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY))
                .build();
        var region = regionRepository
                .findRegionInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new);
        region.setPostsDTO(List.of(post));
        regionRepository.save(region);
        //when
        var actual = service.all(USA_NAME, WASHINGTON_NAME);
        //then
        Long id = postRepository.findAll().get(0).getId();
        assertThat(actual).isEqualTo(List.of(post.getWithId(id).toGetResponse()));
    }


    @Test
    void allById() {
        //given
        final String heading = "first post some heading";
        final BigDecimal money = BigDecimal.valueOf(1020.22);
        final String link = "localH0sT";
        final PostDTO post = PostDTO.builder()
                .money(money)
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .heading(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY))
                .build();
        var region = regionRepository
                .findRegionInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new);
        region.setPostsDTO(List.of(post));
        regionRepository.save(region);
        //when
        var actual = service.all(region.getId());
        //then
        Long id = postRepository.findAll().get(0).getId();
        assertThat(actual).isEqualTo(List.of(post.getWithId(id).toGetResponse()));
    }

    @Test
    void get() {
        //given
        final String heading = "first post some heading";
        final BigDecimal money = BigDecimal.valueOf(1020.22);
        final String link = "localH0sT";
        final PostDTO post = PostDTO.builder()
                .money(money)
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .heading(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY))
                .build();
        var region = regionRepository
                .findRegionInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new);
        region.setPostsDTO(List.of(post));
        regionRepository.save(region);
        Post postDAO = postRepository.findAll().get(0);
        //when
        var actual = service.get(postDAO.getId());
        //then
        Long id = postRepository.findAll().get(0).getId();
        assertThat(actual).isEqualTo(post.getWithId(id));
    }

    @Test
    void updateMoney() {
        //given
        final String heading = "first post some heading";
        final BigDecimal money = BigDecimal.valueOf(120.22);
        final String link = "localH0sT";
        final PostDTO old = PostDTO.builder()
                .money(money)
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .heading(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY))
                .build();

        var region = regionRepository
                .findRegionInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new);
        var country = countryRepository
                .findCountryDTOByNameIgnoreCase(USA_NAME)
                .orElseThrow(RegionNotFoundException::new);
        final BigDecimal oldMoneyOfRegion = region.getMoney();
        final BigDecimal oldMoneyOfCountry = country.getMoney();
        region.setPostsDTO(List.of(old));
        regionRepository.save(region);
        final PostDTO newPost = PostDTO.builder()
                .money(money.multiply(BigDecimal.valueOf(2)))
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .id(postRepository.findAll().get(0).getId())
                .heading(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY))
                .build();
        //when
        service.update(newPost);
        //then
        var regionUpdatedMoney = regionRepository
                .findRegionInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new)
                .getMoney();
        var countryUpdatedMoney = countryRepository
                .findCountryDTOByNameIgnoreCase(USA_NAME)
                .orElseThrow(RegionNotFoundException::new)
                .getMoney();
        assertThat(regionUpdatedMoney.subtract(oldMoneyOfRegion)
                .compareTo(money)).isEqualTo(0);
        assertThat(countryUpdatedMoney.subtract(oldMoneyOfCountry)
                .compareTo(money)).isEqualTo(0);
    }

    @Test
    void updateCategory() {
        //given
        final String heading = "first post some heading";
        final BigDecimal money = BigDecimal.valueOf(120.22);
        final String link = "localH0sT";
        final PostDTO old = PostDTO.builder()
                .money(money)
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .heading(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY))
                .build();
        final BigDecimal newNumber = WASHINGTON_IN_USA_FOOD_CATEGORY
                .getNumber()
                .divide(BigDecimal.valueOf(2), Floats.MONEY_SCALE, Floats.MONEY_ROUNDING);
        var region = regionRepository
                .findRegionInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new);
        region.setPostsDTO(List.of(old));
        regionRepository.save(region);
        final PostDTO newPost = PostDTO.builder()
                .money(money)
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .id(postRepository.findAll().get(0).getId())
                .heading(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY.updateNumber(newNumber)))
                .build();
        //when
        service.update(newPost);
        //then
        final var regionCategories = regionRepository
                .findRegionDTOInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new)
                .getCategories();
        final var countryCategories = countryRepository
                .findCountryDTOByNameIgnoreCase(USA_NAME)
                .orElseThrow(RegionNotFoundException::new)
                .getCategories();
        final var countryCategoriesMap = getCategoryMap(countryCategories);
        final var regionCategoriesMap = getCategoryMap(regionCategories);
        final var currentRegionFoodNumber = regionCategoriesMap.get(FOOD).getNumber();
        final var currentCountryFoodNumber = countryCategoriesMap.get(FOOD).getNumber();
        assertThat(NUMBER_OF_FOOD.subtract(newNumber)
                .compareTo(currentRegionFoodNumber)).isEqualTo(0);
        assertThat(USA_FOOD_CATEGORY.getNumber().subtract(newNumber)
                .compareTo(currentCountryFoodNumber)).isEqualTo(0);
    }

    @Test
    void deleteTestCategories() {
        //given
        final String heading = "first post some heading";
        final BigDecimal money = BigDecimal.valueOf(120.22);
        final String link = "localH0sT";
        final BigDecimal newNumber = WASHINGTON_IN_USA_FOOD_CATEGORY
                .getNumber()
                .divide(BigDecimal.valueOf(2), Floats.MONEY_SCALE, Floats.MONEY_ROUNDING);
        final PostDTO old = PostDTO.builder()
                .money(money)
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .heading(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY.updateNumber(newNumber)))
                .build();
        var region = regionRepository
                .findRegionInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new);
        region.setPostsDTO(List.of(old));
        regionRepository.save(region);
        //when
        service.delete(postRepository.findAll().get(0).getId());
        //then
        final var regionCategories = regionRepository
                .findRegionDTOInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new)
                .getCategories();
        final var countryCategories = countryRepository
                .findCountryDTOByNameIgnoreCase(USA_NAME)
                .orElseThrow(RegionNotFoundException::new)
                .getCategories();
        final var countryCategoriesMap = getCategoryMap(countryCategories);
        final var regionCategoriesMap = getCategoryMap(regionCategories);
        final var currentRegionFoodNumber = regionCategoriesMap.get(FOOD).getNumber();
        final var currentCountryFoodNumber = countryCategoriesMap.get(FOOD).getNumber();
        assertThat(NUMBER_OF_FOOD.subtract(newNumber)
                .compareTo(currentRegionFoodNumber)).isEqualTo(0);
        assertThat(USA_FOOD_CATEGORY.getNumber().subtract(newNumber)
                .compareTo(currentCountryFoodNumber)).isEqualTo(0);
    }

    @Test
    void deleteTestMoney() {
        //given
        final String heading = "first post some heading";
        final BigDecimal money = BigDecimal.valueOf(120.22);
        final String link = "localH0sT";
        final BigDecimal newNumber = WASHINGTON_IN_USA_FOOD_CATEGORY
                .getNumber()
                .divide(BigDecimal.valueOf(2), Floats.MONEY_SCALE, Floats.MONEY_ROUNDING);
        final PostDTO old = PostDTO.builder()
                .money(money)
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .heading(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY.updateNumber(newNumber)))
                .build();
        var region = regionRepository
                .findRegionInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new);
        region.setPostsDTO(List.of(old));
        regionRepository.save(region);
        //when
        service.delete(postRepository.findAll().get(0).getId());
        //then
        final var regionDTO = regionRepository
                .findRegionDTOInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new);
        final var countryDTO = countryRepository
                .findCountryDTOByNameIgnoreCase(USA_NAME)
                .orElseThrow(RegionNotFoundException::new);
        assertThat(countryDTO.getMoney().compareTo(USA_MONEY.subtract(money))).isEqualTo(0);
        assertThat(regionDTO.getMoney().compareTo(WASHINGTON_IN_USA_MONEY.subtract(money))).isEqualTo(0);
    }

    @Test
    void deleteAllByRegionIdTestMoney() {
        //given
        final String heading = "first post some heading";
        final BigDecimal money = BigDecimal.valueOf(120.22);
        final String link = "localH0sT";
        final BigDecimal newNumber = WASHINGTON_IN_USA_FOOD_CATEGORY
                .getNumber()
                .divide(BigDecimal.valueOf(2), Floats.MONEY_SCALE, Floats.MONEY_ROUNDING);
        final PostDTO old = PostDTO.builder()
                .money(money)
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .heading(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY.updateNumber(newNumber)))
                .build();
        var region = regionRepository
                .findRegionInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new);
        region.setPostsDTO(List.of(old));
        regionRepository.save(region);
        //when
        service.deleteAllByRegionId(region.getId());
        //then
        final var regionDTO = regionRepository
                .findRegionDTOInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new);
        final var countryDTO = countryRepository
                .findCountryDTOByNameIgnoreCase(USA_NAME)
                .orElseThrow(RegionNotFoundException::new);
        assertThat(countryDTO.getMoney().compareTo(USA_MONEY.subtract(money))).isEqualTo(0);
        assertThat(regionDTO.getMoney().compareTo(WASHINGTON_IN_USA_MONEY.subtract(money))).isEqualTo(0);
    }

    @Test
    void deleteAllByNamesTestCategories() {
        //given
        final String heading = "first post some heading";
        final BigDecimal money = BigDecimal.valueOf(120.22);
        final String link = "localH0sT";
        final BigDecimal newNumber = WASHINGTON_IN_USA_FOOD_CATEGORY
                .getNumber()
                .divide(BigDecimal.valueOf(2), Floats.MONEY_SCALE, Floats.MONEY_ROUNDING);
        final PostDTO old = PostDTO.builder()
                .money(money)
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .heading(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY.updateNumber(newNumber)))
                .build();
        var region = regionRepository
                .findRegionInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new);
        region.setPostsDTO(List.of(old));
        regionRepository.save(region);
        //when
        service.deleteAll(USA_NAME, WASHINGTON_NAME);
        //then
        final var regionCategories = regionRepository
                .findRegionDTOInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new)
                .getCategories();
        final var countryCategories = countryRepository
                .findCountryDTOByNameIgnoreCase(USA_NAME)
                .orElseThrow(RegionNotFoundException::new)
                .getCategories();
        final var countryCategoriesMap = getCategoryMap(countryCategories);
        final var regionCategoriesMap = getCategoryMap(regionCategories);
        final var currentRegionFoodNumber = regionCategoriesMap.get(FOOD).getNumber();
        final var currentCountryFoodNumber = countryCategoriesMap.get(FOOD).getNumber();
        assertThat(NUMBER_OF_FOOD.subtract(newNumber)
                .compareTo(currentRegionFoodNumber)).isEqualTo(0);
        assertThat(USA_FOOD_CATEGORY.getNumber().subtract(newNumber)
                .compareTo(currentCountryFoodNumber)).isEqualTo(0);
    }
}