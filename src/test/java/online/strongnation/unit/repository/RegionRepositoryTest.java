package online.strongnation.unit.repository;

import online.strongnation.model.dto.RegionDTO;
import online.strongnation.model.entity.Country;
import online.strongnation.model.entity.Region;
import online.strongnation.exception.RegionNotFoundException;
import online.strongnation.repository.CountryRepository;
import online.strongnation.repository.RegionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class RegionRepositoryTest {
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private RegionRepository regionRepository;

    private final String USA_NAME = "United States of America";
    private final String POLAND_NAME = "Poland";
    private final String WASHINGTON_NAME = "Washington D.C.";
    private final String WARSAW_NAME = "Warsaw";

    @AfterEach
    void tearDown() {
        countryRepository.deleteAll();
    }

    @Test
    void existsCountryWithRegion() {
        //given
        final String name = "NAME";
        final Country country = new Country("NaMe");
        String regionName = "some ReGion";
        Region region = new Region(regionName);
        country.setRegions(List.of(region));
        countryRepository.save(country);
        //when
        boolean b = regionRepository.existsRegionInCountryByNamesIgnoringCase(name, regionName);
        //then
        assertThat(b).isTrue();
    }

    @Test
    void findDTO() {
        //given
        final String name = "NAME";
        final Country country = new Country("NaMe");
        String regionName = "some ReGion";
        Region region = new Region(regionName);
        country.setRegions(List.of(region));
        countryRepository.save(country);
        var expected = new RegionDTO(region);
        //when
        final var actual = regionRepository
                .findRegionDTOInCountryByNamesIgnoringCase(name, regionName)
                .orElseThrow(RegionNotFoundException::new);
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllDTO() {
        //given
        //usa
        Region washington = new Region(WASHINGTON_NAME);
        Region warsawInUSA = new Region(WARSAW_NAME);
        Country USACountry = new Country(USA_NAME);
        List<Region> USARegions = List.of(washington, warsawInUSA);
        USACountry.setRegions(USARegions);
        //poland
        Country polandCountry = new Country(POLAND_NAME);
        Region washingtonInPoland = new Region(WASHINGTON_NAME);
        Region warsawInPoland = new Region(WARSAW_NAME);
        List<Region> polandRegions = List.of(washingtonInPoland, warsawInPoland);
        polandCountry.setRegions(polandRegions);
        countryRepository.saveAll(List.of(polandCountry, USACountry));
        //when
        final var actualUSAList = regionRepository.findAllRegionDTOByCountryNameIgnoringCase(USA_NAME);
        final var actualPolandList = regionRepository.findAllRegionDTOByCountryNameIgnoringCase(POLAND_NAME);
        //then
        assertThat(USARegions.stream().map(RegionDTO::new)
                .toList().containsAll(actualUSAList)).isTrue();
        assertThat(polandRegions.stream().map(RegionDTO::new)
                .toList().containsAll(actualPolandList)).isTrue();
    }

    @Test
    void findDTOById() {
        //given
        final Country country = new Country("NaMe");
        countryRepository.save(country);
        String regionName = "some ReGion";
        Region region = new Region(regionName);
        region.setCountry(country);
        var expected = new RegionDTO(regionRepository.save(region));
        //when
        var actual = regionRepository.findRegionDTOById(expected.getId())
                .orElseThrow(RegionNotFoundException::new);
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void renameById() {
        //given
        String countryName = "NaMe";
        final Country country = new Country(countryName);
        countryRepository.save(country);
        String regionName = "some ReGion";
        String newRegionName = "some 2";
        Region region = new Region(regionName);
        region.setCountry(country);
        regionRepository.save(region);
        //when
        regionRepository.updateNameOfRegionById(region.getId(), newRegionName);
        //then
        var oldExists = regionRepository.existsRegionInCountryByNamesIgnoringCase(countryName, regionName);
        var newExists = regionRepository.existsRegionInCountryByNamesIgnoringCase(countryName, newRegionName);
        assertThat(oldExists).isFalse();
        assertThat(newExists).isTrue();
    }
}
