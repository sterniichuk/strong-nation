package online.strongnation.unit.entity;

import online.strongnation.entity.Category;
import online.strongnation.entity.Country;
import online.strongnation.entity.Region;
import online.strongnation.entity.RegionCategory;
import online.strongnation.repository.CategoryRepository;
import online.strongnation.repository.CountryRepository;
import online.strongnation.repository.RegionCategoryRepository;
import online.strongnation.repository.RegionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class CategoryDataSavingTest {
    @Autowired
    private RegionRepository repository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RegionCategoryRepository regionCategoryRepository;
    @Autowired
    private CountryRepository countryRepository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
        regionCategoryRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void saveWithCategory() {
        Country country = new Country();
        country.setName("Ukraine");
        Region region = new Region();
        country.setRegions(List.of(region));
        String name = "Rivne";
        region.setName(name);
        BigDecimal money = new BigDecimal(1000);
        region.setMoney(money);

        RegionCategory regionCategory = new RegionCategory();
        Category category = new Category();
        final float number = 9.f;
        final String categoryName = "food";
        final String unit = "kg";
        category.setNumber(number);
        category.setName(categoryName);
        category.setUnits(unit);
        regionCategory.setCategory(category);

        region.setCategories(List.of(regionCategory));
        countryRepository.save(country);

        Category category2 = new Category();
        final float newNumb = 10.f;
        category2.setName(categoryName);
        category2.setUnits(unit);
        category2.setNumber(newNumb);
        RegionCategory regionCategory2 = new RegionCategory(category2);

        Region region1 = repository.findAll().get(0);
        List<RegionCategory> regionCategory21 = new ArrayList<>();
        regionCategory21.add(regionCategory2);
        region1.setCategories(regionCategory21);
        repository.save(region1);

        List<Category> categories = categoryRepository.findAll();
        assertThat(categories.size()).isEqualTo(1);
    }
}
