package online.strongnation.entity;

import online.strongnation.repository.CategoryRepository;
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

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void simpleSave() {
        Region region = new Region();
        String name = "Rivne";
        region.setName(name);
        BigDecimal money = new BigDecimal(1000);
        region.setMoney(money);
        repository.save(region);
        List<Region> all = repository.findAll();
        List<Region> list = all.stream()
                .filter(x -> name.equals(x.getName()))
                .filter(x -> money.equals(x.getMoney())).toList();
        assertThat(list.size()).isEqualTo(1);
        Region regionSaved = list.get(0);
        assertThat(regionSaved.getName()).isEqualTo(name);
        assertThat(regionSaved.getMoney()).isEqualTo(money);
        assertThat(regionSaved.getId()).isNotEqualTo(null);
        assertThat(regionSaved.getCategories()).isEqualTo(null);
    }

    @Test
    void saveWithCategory() {
        Region region = new Region();
        String name = "Rivne";
        region.setName(name);
        BigDecimal money = new BigDecimal(1000);
        region.setMoney(money);

        RegionCategory regionCategory = new RegionCategory();
        Category category = new Category();
        category.setNumber(9.f);
        category.setName("food");
        category.setUnits("kg");
        regionCategory.setCategory(category);

        region.setCategories(List.of(regionCategory));
        repository.save(region);

        List<Region> all = repository.findAll();
        List<Region> list = all.stream()
                .filter(x -> name.equals(x.getName()))
                .filter(x -> money.equals(x.getMoney())).toList();
        assertThat(list.size()).isEqualTo(1);
        Region regionSaved = list.get(0);
        assertThat(regionSaved.getCategories()).isNotEqualTo(null);
        assertThat(regionSaved.getCategories().size()).isEqualTo(1);

        var regionCategorySaved = regionSaved.getCategories().get(0);

        assertThat(regionCategorySaved.getCategory()).isNotEqualTo(null);
        var savedCategory = regionCategorySaved.getCategory();
        assertThat(savedCategory).isEqualTo(category);

        Category category1 = categoryRepository.findAll()
                .stream().filter(x->category.getName().equals(x.getName()))
                .findFirst().orElseThrow(IllegalStateException::new);
        assertThat(category).isEqualTo(category1);
    }


}