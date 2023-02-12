package online.strongnation.integration;

import online.strongnation.business.config.Floats;
import online.strongnation.business.exception.CountryNotFoundException;
import online.strongnation.business.exception.RegionNotFoundException;
import online.strongnation.business.model.dto.CategoryDTO;
import online.strongnation.business.model.dto.CountryDTO;
import online.strongnation.business.model.dto.PostDTO;
import online.strongnation.business.model.dto.RegionDTO;
import online.strongnation.business.model.entity.Country;
import online.strongnation.business.model.entity.Post;
import online.strongnation.business.repository.CountryRepository;
import online.strongnation.business.repository.PostRepository;
import online.strongnation.business.repository.RegionRepository;
import online.strongnation.business.service.CountryService;
import online.strongnation.business.service.PostService;
import online.strongnation.business.service.RegionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static online.strongnation.business.service.implementation.CategoryUtils.getCategoryMap;
import static online.strongnation.business.service.implementation.RequestParameterFixer.checkDate;
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
            .number(BigDecimal.valueOf(100))
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
            .categories(CATEGORIES_OF_WASHINGTON_OF_USA)
            .name(WASHINGTON_NAME)
            .build();
    private final RegionDTO WARSAW_IN_USA = RegionDTO.builder()
            .categories(CATEGORIES_OF_WARSAW_OF_USA)
            .name(WARSAW_NAME)
            .build();
    private final CountryDTO USA = CountryDTO.builder()
            .categories(CATEGORIES_OF_USA)
            .name(USA_NAME)
            .build();
    //poland
    private final RegionDTO WASHINGTON_IN_POLAND = RegionDTO.builder()
            .categories(CATEGORIES_OF_WASHINGTON_OF_POLAND)
            .name(WASHINGTON_NAME)
            .build();
    private final RegionDTO WARSAW_IN_POLAND = RegionDTO.builder()
            .categories(CATEGORIES_OF_WARSAW_OF_POLAND)
            .name(WARSAW_NAME)
            .build();
    private final CountryDTO POLAND = CountryDTO.builder()
            .categories(CATEGORIES_OF_POLAND)
            .name(POLAND_NAME)
            .build();

    @Autowired
    private PostService postService;
    @Autowired
    private CountryService countryService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        final Country USA_ENTITY = new Country(USA);
        USA_ENTITY.setRegionsDTO(List.of(WASHINGTON_IN_USA, WARSAW_IN_USA));
        countryRepository.save(USA_ENTITY);
    }

    @AfterEach
    void tearDown() {
        countryRepository.deleteAll();
    }

    @Test
    void createTestCategoriesByNames() {
        //given
        final String heading = "first post some heading";
        final String link = "localH0sT";
        final PostDTO post = PostDTO.builder()
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .description(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY))
                .build();
        //when
        PostDTO actual = postService.create(post, USA_NAME, WASHINGTON_NAME);
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
    void createTestCategoriesById() {
        //given
        final String heading = "first post some heading";
        final String link = "localH0sT";
        final PostDTO post = PostDTO.builder()
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .description(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY))
                .build();
        Long regionId = regionRepository
                .findRegionDTOInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new)
                .getId();
        //when
        PostDTO actual = postService.create(post, regionId);
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
        final String link = "localH0sT";
        final PostDTO post = PostDTO.builder()
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .description(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY))
                .build();
        var region = regionRepository
                .findRegionInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new);
        region.setPostsDTO(List.of(post));
        regionRepository.save(region);
        //when
        var actual = postService.all(USA_NAME, WASHINGTON_NAME);
        //then
        Long id = postRepository.findAll().get(0).getId();
        assertThat(actual).isEqualTo(List.of(post.getWithId(id).toGetResponse()));
    }


    @Test
    void allById() {
        //given
        final String heading = "first post some heading";
        final String link = "localH0sT";
        final PostDTO post = PostDTO.builder()
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .description(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY))
                .build();
        var region = regionRepository
                .findRegionInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new);
        region.setPostsDTO(List.of(post));
        regionRepository.save(region);
        //when
        var actual = postService.all(region.getId());
        //then
        Long id = postRepository.findAll().get(0).getId();
        assertThat(actual).isEqualTo(List.of(post.getWithId(id).toGetResponse()));
    }

    @Test
    void get() {
        //given
        final String heading = "first post some heading";
        final String link = "localH0sT";
        final PostDTO post = PostDTO.builder()
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .description(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY))
                .build();
        var region = regionRepository
                .findRegionInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new);
        region.setPostsDTO(List.of(post));
        regionRepository.save(region);
        Post postDAO = postRepository.findAll().get(0);
        //when
        var actual = postService.get(postDAO.getId());
        //then
        Long id = postRepository.findAll().get(0).getId();
        assertThat(actual).isEqualTo(post.getWithId(id));
    }


    @Test
    void updateCategory() {
        //given
        final String heading = "first post some heading";
        final String link = "localH0sT";
        final PostDTO old = PostDTO.builder()
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .description(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY))
                .build();
        final BigDecimal newNumber = WASHINGTON_IN_USA_FOOD_CATEGORY
                .getNumber()
                .divide(BigDecimal.valueOf(2), Floats.CATEGORY_SCALE, Floats.CATEGORY_ROUNDING);
        var region = regionRepository
                .findRegionInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new);
        region.setPostsDTO(List.of(old));
        regionRepository.save(region);
        final PostDTO newPost = PostDTO.builder()
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .id(postRepository.findAll().get(0).getId())
                .description(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY.updateNumber(newNumber)))
                .build();
        //when
        postService.update(newPost);
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
        final String link = "localH0sT";
        final BigDecimal newNumber = WASHINGTON_IN_USA_FOOD_CATEGORY
                .getNumber()
                .divide(BigDecimal.valueOf(2), Floats.CATEGORY_SCALE, Floats.CATEGORY_ROUNDING);
        final PostDTO old = PostDTO.builder()
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .description(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY.updateNumber(newNumber)))
                .build();
        var region = regionRepository
                .findRegionInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new);
        region.setPostsDTO(List.of(old));
        regionRepository.save(region);
        //when
        postService.delete(postRepository.findAll().get(0).getId());
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
    void deleteAllByNamesTestCategories() {
        //given
        final String heading = "first post some heading";
        final String link = "localH0sT";
        final BigDecimal newNumber = WASHINGTON_IN_USA_FOOD_CATEGORY
                .getNumber()
                .divide(BigDecimal.valueOf(2), Floats.CATEGORY_SCALE, Floats.CATEGORY_ROUNDING);

        final PostDTO old = PostDTO.builder()
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .description(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY.updateNumber(newNumber)))
                .build();
        var region = regionRepository
                .findRegionInCountryByNamesIgnoringCase(USA_NAME, WASHINGTON_NAME)
                .orElseThrow(RegionNotFoundException::new);
        region.setPostsDTO(List.of(old));
        regionRepository.save(region);
        //when
        postService.deleteAll(USA_NAME, WASHINGTON_NAME);
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
    void updateCategoryDifficultTest() {
        //given
        final String heading = "first post some heading";
        final String link = "localH0sT";
        final PostDTO old = PostDTO.builder()
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .description(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY, WARSAW_IN_USA_CARS_CATEGORY))
                .build();
        final PostDTO specialPost = PostDTO.builder()
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .description(heading)
                .categories(List.of(WASHINGTON_IN_USA_FOOD_CATEGORY, WARSAW_IN_USA_CARS_CATEGORY,
                        CategoryDTO.builder()
                                .name("guns")
                                .number(BigDecimal.valueOf(1000_000))
                                .build()))
                .build();

        final String COUNTRY_NAME = "some random country name";
        final String REGION_NAME1 = "some random region name";
        final String REGION_NAME2 = "some random region2 name";
        countryService.create(COUNTRY_NAME);
        Long region1Id = regionService.create(COUNTRY_NAME, REGION_NAME1).getId();
        Long region2Id = regionService.create(COUNTRY_NAME, REGION_NAME2).getId();
        for (int i = 0; i < 2; i++) {
            postService.create(old, region1Id);
        }
        for (int i = 0; i < 2; i++) {
            postService.create(old, COUNTRY_NAME, REGION_NAME2);
        }
        final var expectedCountry = countryService.get(COUNTRY_NAME);
        final var expectedRegion = regionService.get(COUNTRY_NAME, REGION_NAME2);
        PostDTO specialDTOSaved = postService.create(specialPost, region2Id);
        final PostDTO updatedSpecialPost = PostDTO.builder()
                .id(specialDTOSaved.getId())
                .date(checkDate(LocalDateTime.now()))
                .link(link)
                .description(heading)
                .categories(List.of())
                .build();
        //when
        postService.update(updatedSpecialPost);
        //then
        final var actualRegion = regionRepository
                .findRegionDTOInCountryByNamesIgnoringCase(COUNTRY_NAME, REGION_NAME2)
                .orElseThrow(RegionNotFoundException::new);
        final var actualCountry = countryRepository
                .findCountryDTOByNameIgnoreCase(COUNTRY_NAME)
                .orElseThrow(RegionNotFoundException::new);
        assertThat(actualRegion).isEqualTo(expectedRegion);
        assertThat(actualCountry).isEqualTo(expectedCountry);
    }
}