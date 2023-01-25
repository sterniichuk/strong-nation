package online.strongnation.unit.entity;

import online.strongnation.dto.CategoryDTO;
import online.strongnation.entity.Category;
import online.strongnation.entity.Country;
import online.strongnation.entity.CountryCategory;
import online.strongnation.exception.CountryNotFoundException;
import online.strongnation.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
public class CountryDataSavingTest {

    @Autowired
    private CountryRepository countryRepository;

    @Test
    void testLazyCollectionLoading() {
        //given
        Country country = new Country();
        String name = "Ukraine";
        country.setName(name);
        CountryCategory regionCategory = new CountryCategory();
        Category category = new Category();
        category.setNumber(9.f);
        category.setName("food");
        category.setUnits("kg");
        regionCategory.setCategory(category);

        country.setCategories(List.of(regionCategory));
        countryRepository.save(country);
        //when
        Country countrySaved = countryRepository.findCountryByName(name)
                .orElseThrow(CountryNotFoundException::new);
        List<CategoryDTO> categories = countrySaved
                .getCategories().stream()
                .map(CountryCategory::getCategory)
                .map(x -> new CategoryDTO(x.getName(), x.getNumber(), x.getUnits()))
                .toList();
        //then
        assertThat(categories.isEmpty()).isFalse();
    }
}
