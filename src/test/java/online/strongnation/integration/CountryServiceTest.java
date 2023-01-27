package online.strongnation.integration;

import online.strongnation.config.EntityNameLength;
import online.strongnation.model.dto.CountryDTO;
import online.strongnation.model.entity.Country;
import online.strongnation.exception.IllegalCountryException;
import online.strongnation.repository.CountryRepository;
import online.strongnation.service.CountryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class CountryServiceTest {

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CountryService service;
    private final String USAName = "United States of America";

    @BeforeEach
    void setUp() {
        String polandName = "Poland";
        Country polandCountry = new Country(polandName);
        Country USACountry = new Country(USAName);
        countryRepository.saveAll(List.of(polandCountry, USACountry));
    }

    @AfterEach
    void tearDown() {
        countryRepository.deleteAll();
    }

    @Test
    void createTestSave() {
        //given
        final String actualName = "   test         Country       ";
        final String expectedName = "test Country";
        //when
        service.create(actualName);
        //then
        List<Country> all = countryRepository.findAll();
        assertThat(all.size()).isEqualTo(3);
        final var actual = all.stream().filter(x -> x.getName().equals(expectedName)).findAny();
        assertThat(actual).isNotEmpty();
    }

    @Test
    void createTestReturnValue() {
        //given
        final String actualName = "   test         Country       ";
        final String expectedName = "test Country";
        //when
        CountryDTO actual = service.create(actualName);
        //then
        assertThat(actual.getName()).isEqualTo(expectedName);
    }

    @Test
    void createAlreadyExistedCountryLowerCase() {
        //given
        final var input = USAName.toLowerCase();
        //when
        //then
        assertThatThrownBy(() -> service.create(input))
                .isInstanceOf(IllegalCountryException.class)
                .hasMessageMatching("Country " + input + " already exists");
    }

    @Test
    void createAlreadyExistedCountryUpperCase() {
        //given
        final var input = USAName.toUpperCase();
        //when
        //then
        assertThatThrownBy(() -> service.create(input))
                .isInstanceOf(IllegalCountryException.class)
                .hasMessageMatching("Country " + input + " already exists");
    }

    @Test
    void createWithTooLongCountry() {
        //given
        byte[] array = new byte[EntityNameLength.REGION.length + 1];
        Arrays.fill(array, (byte) 'A');
        final var input = new String(array, StandardCharsets.UTF_8);
        //when
        //then
        assertThatThrownBy(() -> service.create(input))
                .isInstanceOf(IllegalCountryException.class)
                .hasMessage("Too long name of country");
    }


    @Test
    void get() {
        //given
        final var USA = countryRepository.findCountryByName(USAName).orElseThrow(IllegalCountryException::new);
        final var expected = CountryDTO.builder().name(USAName).id(USA.getId()).categories(List.of()).build();
        //when
        CountryDTO actual = service.get(USAName);
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getAll() {
        //given
        final List<CountryDTO> expected = countryRepository.findAllDTO();
        //when
        final List<CountryDTO> actual = service.getAll();
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void rename() {
        //given
        final String newName = "Some new name";
        final var expected = countryRepository
                .findCountryDTOByNameIgnoreCase(USAName)
                .orElseThrow(IllegalCountryException::new)
                .toBuilder()
                .name(newName)
                .build();
        //when
        CountryDTO actual = service.rename(USAName, newName);
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void renameWithBlankSymbols() {
        //given
        final String expectedName = "Some new name";
        final String givenNewName = "        Some  \n\t new \t  name    \n";
        final var expected = countryRepository
                .findCountryDTOByNameIgnoreCase(USAName)
                .orElseThrow(IllegalCountryException::new)
                .toBuilder()
                .name(expectedName)
                .build();
        //when
        CountryDTO actual = service.rename(USAName, givenNewName);
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void renameWithTooLongCountryOld() {
        //given
        byte[] array = new byte[EntityNameLength.COUNTRY.length + 1];
        Arrays.fill(array, (byte) 'A');
        final var input = new String(array, StandardCharsets.UTF_8);
        //when
        //then
        assertThatThrownBy(() -> service.rename(input, "Normal"))
                .isInstanceOf(IllegalCountryException.class);
    }

    @Test
    void renameWithTooLongCountryNew() {
        //given
        byte[] array = new byte[EntityNameLength.COUNTRY.length + 1];
        Arrays.fill(array, (byte) 'A');
        final String input = new String(array);
        //when
        //then
        assertThatThrownBy(() -> service.rename("Normal", input))
                .isInstanceOf(IllegalCountryException.class);
    }

    @Test
    void testReturnValueOfDelete() {
        //given
        final var USA = countryRepository.findCountryByName(USAName).orElseThrow(IllegalCountryException::new);
        final var expected = CountryDTO.builder().name(USAName).id(USA.getId()).categories(List.of()).build();
        //when
        CountryDTO actual = service.delete(USAName);
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testDeleting() {
        //when
        service.delete(USAName);
        //then
        boolean empty = countryRepository
                .findCountryByName(USAName).isEmpty();
        assertThat(empty).isTrue();
    }

    @Test
    void testReturnValueOfDeletingAll() {
        //given
        final var expected = countryRepository.findAllDTO();
        //when
        final var actual = service.deleteAll();
        //then
        assertThat(actual).hasSameClassAs(expected);
    }

    @Test
    void deleteAll() {
        //given
        //when
        service.deleteAll();
        //then
        boolean empty = countryRepository.findAll().isEmpty();
        assertThat(empty).isTrue();
    }
}