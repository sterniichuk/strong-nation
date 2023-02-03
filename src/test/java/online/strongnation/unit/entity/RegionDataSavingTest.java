package online.strongnation.unit.entity;

import online.strongnation.model.entity.CategoryDAO;
import online.strongnation.model.entity.Country;
import online.strongnation.model.entity.Region;
import online.strongnation.model.entity.RegionCategory;
import online.strongnation.repository.CategoryRepository;
import online.strongnation.repository.CountryRepository;
import online.strongnation.repository.RegionRepository;
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
        BigDecimal money = new BigDecimal(1000);
        region.setMoney(money);
        countryRepository.save(country);
        List<Region> all = repository.findAll();
        List<Region> list = all.stream()
                .filter(x -> name.equals(x.getName()))
                .filter(x -> money.equals(x.getMoney())).toList();
        assertThat(list.size()).isEqualTo(1);
        Region regionSaved = list.get(0);
        assertThat(regionSaved.getName()).isEqualTo(name);
        assertThat(regionSaved.getMoney()).isEqualTo(money);
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
        BigDecimal money = new BigDecimal(1000);
        region.setMoney(money);

        RegionCategory regionCategory = new RegionCategory();
        CategoryDAO categoryDAO = new CategoryDAO();
        categoryDAO.setNumber(BigDecimal.valueOf(9.f));
        categoryDAO.setName("food");
        categoryDAO.setUnits("kg");
        regionCategory.setCategoryDAO(categoryDAO);

        region.setCategories(List.of(regionCategory));
        countryRepository.save(country);

        List<Region> all = repository.findAll();
        List<Region> list = all.stream()
                .filter(x -> name.equals(x.getName()))
                .filter(x -> money.equals(x.getMoney())).toList();
        assertThat(list.size()).isEqualTo(1);
        Region regionSaved = list.get(0);
        assertThat(regionSaved.getCategories()).isNotEqualTo(null);
        assertThat(regionSaved.getCategories().size()).isEqualTo(1);

        var regionCategorySaved = regionSaved.getCategories().get(0);

        assertThat(regionCategorySaved.getCategoryDAO()).isNotEqualTo(null);
        var savedCategory = regionCategorySaved.getCategoryDAO();
        assertThat(savedCategory).isEqualTo(categoryDAO);

        CategoryDAO categoryDAO1 = categoryRepository.findAll()
                .stream().filter(x -> categoryDAO.getName().equals(x.getName()))
                .findFirst().orElseThrow(IllegalStateException::new);
        assertThat(categoryDAO).isEqualTo(categoryDAO1);
    }
}