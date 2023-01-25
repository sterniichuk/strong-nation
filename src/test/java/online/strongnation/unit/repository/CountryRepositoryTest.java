package online.strongnation.unit.repository;

import online.strongnation.dto.CategoryDTO;
import online.strongnation.dto.CountryDTO;
import online.strongnation.entity.Category;
import online.strongnation.entity.Country;
import online.strongnation.entity.CountryCategory;
import online.strongnation.exception.CountryNotFoundException;
import online.strongnation.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    @Test
    void existsCountryByNameIgnoreCase() {
        //given
        final String name = "NAME";
        final Country country = new Country("NaMe");
        countryRepository.save(country);
        //when
        boolean b = countryRepository.existsCountryByNameIgnoreCase(name);
        //then
        assertThat(b).isTrue();
    }

    @Test
    void findCountryByNameIgnoreCase() {
        //given
        final String name = "NAME";
        final Country country = new Country("NaMe");
        countryRepository.save(country);
        //when
        var b = countryRepository.findCountryByNameIgnoreCase(name);
        //then
        assertThat(b.orElseThrow(CountryNotFoundException::new)).isEqualTo(country);
    }

    @Test
    void renameCountry() {
        //given
        final String name = "NAME";
        final String newName = "newName";
        final Country country = new Country("NaMe");
        countryRepository.save(country);
        //when
        countryRepository.updateNameOfCountry(name, newName);
        //then
        var oldExists = countryRepository.existsCountryByNameIgnoreCase(name);
        var newExists = countryRepository.existsCountryByNameIgnoreCase(newName);
        assertThat(oldExists).isFalse();
        assertThat(newExists).isTrue();
    }

    @Test
    void projection() {
        //given
        final String name = "NAME";
        final Country country = new Country("NaMe");
        final var categoryInner = new Category(
                "food",
                3f,
                "kg"
        );
        final var categoryDTO = new CategoryDTO(categoryInner);
        final CountryCategory category =
                new CountryCategory(
                        categoryInner
                );
        country.setCategories(List.of(category));
        countryRepository.save(country);
        //when
        Optional<CountryDTO> countryDTO = countryRepository.findCountryDTOByNameIgnoreCase(name);
        //then
        assertThat(countryDTO).isNotEmpty();
        assertThat(countryDTO.orElseThrow(CountryNotFoundException::new)
                .getCategories().get(0)).isEqualTo(categoryDTO);
    }

    @Test
    void projections() {
        //given
        final Country country = new Country("NaMe");
        final var categoryInner = new Category(
                "food",
                3f,
                "kg"
        );
        final CountryCategory category =
                new CountryCategory(
                        categoryInner
                );
        country.setCategories(List.of(category));
        countryRepository.save(country);
        final var expected = List.of(new CountryDTO(country));
        //when
        var actual = countryRepository.findAllDTO();
        //then
        assertThat(actual).isEqualTo(expected);
    }


}