package online.strongnation.unit.entity;

import online.strongnation.business.model.entity.CategoryEntity;
import online.strongnation.business.model.entity.Country;
import online.strongnation.business.model.entity.Region;
import online.strongnation.business.model.entity.RegionCategory;
import online.strongnation.business.repository.CategoryRepository;
import online.strongnation.business.repository.CountryRepository;
import online.strongnation.business.repository.RegionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class RegionDataSavingTest {
    @Autowired
    private RegionRepository repository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CountryRepository countryRepository;

    @AfterEach
    void tearDown() {
        countryRepository.deleteAll();
    }

    @Test
    void simpleSave() {
        Country country = new Country();
        country.setName("Ukraine");
        Region region = new Region();
        country.setRegions(List.of(region));
        String name = "Rivne";
        region.setName(name);
        countryRepository.save(country);
        List<Region> all = repository.findAll();
        List<Region> list = all.stream()
                .filter(x -> name.equals(x.getName())).toList();
        assertThat(list.size()).isEqualTo(1);
        Region regionSaved = list.get(0);
        assertThat(regionSaved.getName()).isEqualTo(name);
        assertThat(regionSaved.getId()).isNotEqualTo(null);
        assertThat(regionSaved.getCategories().isEmpty()).isTrue();
    }

    @Test
    void saveWithCategory() {
        Country country = new Country();
        country.setName("Ukraine");
        Region region = new Region();
        country.setRegions(List.of(region));
        String name = "Rivne";
        region.setName(name);
        RegionCategory regionCategory = new RegionCategory();
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setNumber(BigDecimal.valueOf(9.f));
        categoryEntity.setName("food");
        categoryEntity.setUnits("kg");
        regionCategory.setCategoryEntity(categoryEntity);

        region.setCategories(List.of(regionCategory));
        countryRepository.save(country);

        List<Region> all = repository.findAll();
        List<Region> list = all.stream()
                .filter(x -> name.equals(x.getName())).toList();
        assertThat(list.size()).isEqualTo(1);
        Region regionSaved = list.get(0);
        assertThat(regionSaved.getCategories()).isNotEqualTo(null);
        assertThat(regionSaved.getCategories().size()).isEqualTo(1);

        var regionCategorySaved = regionSaved.getCategories().get(0);

        assertThat(regionCategorySaved.getCategoryEntity()).isNotEqualTo(null);
        var savedCategory = regionCategorySaved.getCategoryEntity();
        assertThat(savedCategory).isEqualTo(categoryEntity);

        CategoryEntity categoryEntity1 = categoryRepository.findAll()
                .stream().filter(x -> categoryEntity.getName().equals(x.getName()))
                .findFirst().orElseThrow(IllegalStateException::new);
        assertThat(categoryEntity).isEqualTo(categoryEntity1);
    }
}