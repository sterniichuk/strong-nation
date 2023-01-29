package online.strongnation.service.implementation;

import lombok.RequiredArgsConstructor;
import online.strongnation.exception.CountryNotFoundException;
import online.strongnation.exception.IllegalRegionException;
import online.strongnation.exception.RegionNotFoundException;
import online.strongnation.model.dto.RegionDTO;
import online.strongnation.model.entity.Country;
import online.strongnation.model.entity.Region;
import online.strongnation.repository.CountryRepository;
import online.strongnation.repository.RegionRepository;
import online.strongnation.service.RegionService;
import online.strongnation.service.StatisticService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static online.strongnation.service.implementation.NameFixer.checkAndNormalizeCountry;
import static online.strongnation.service.implementation.NameFixer.checkAndNormalizeRegion;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;
    private final CountryRepository countryRepository;
    private final StatisticService statistic;

    @Override
    public RegionDTO create(final String countryName, final String name) {
        String clearCountryName = checkAndNormalizeCountry(countryName);
        String clearRegionName = checkAndNormalizeRegion(name);
        Country country = findCountryByName(clearCountryName);
        if (regionRepository.existsRegionInCountryByNamesIgnoringCase(clearCountryName, clearRegionName)) {
            throw new IllegalRegionException("Region " + name + " already exists");
        }
        Region region = new Region(clearRegionName);
        region.setCountry(country);
        regionRepository.save(region);
        return new RegionDTO(region);
    }

    private Country findCountryByName(String name) {
        return countryRepository.findCountryByNameIgnoreCase(name)
                .orElseThrow(() -> {
                    throw new CountryNotFoundException("Country " + name + " doesn't exist");
                });
    }

    @Override
    public List<RegionDTO> createAll(String countryName, List<String> names) {
        String clearCountryName = checkAndNormalizeCountry(countryName);
        List<String> clearedRegions = names.stream().map(NameFixer::checkAndNormalizeRegion).toList();
        Country country = findCountryByName(clearCountryName);
        final ArrayList<Region> regions = new ArrayList<>(clearedRegions.size());
        clearedRegions.forEach(s -> {
            if (regionRepository.existsRegionInCountryByNamesIgnoringCase(clearCountryName, s)) {
                throw new IllegalRegionException("Region " + s + " is already present. Regions are not saved");
            }
            Region r = new Region(s);
            r.setCountry(country);
            regions.add(r);
        });
        regionRepository.saveAll(regions);
        return regions.stream().map(RegionDTO::new).toList();
    }

    @Override
    public RegionDTO get(String countryName, String name) {
        String clearCountryName = checkAndNormalizeCountry(countryName);
        String clearRegionName = checkAndNormalizeRegion(name);
        return findRegionInCountryByNames(clearCountryName, clearRegionName);
    }

    private RegionDTO findRegionInCountryByNames(String countryName, String name) {
        return regionRepository.findRegionDTOInCountryByNamesIgnoringCase(countryName, name)
                .orElseThrow(() -> {
                    String message = "Region " + name + " in country " + countryName + " not found";
                    throw new RegionNotFoundException(message);
                });
    }

    @Override
    public RegionDTO get(Long id) {
        return regionRepository.findRegionDTOById(id)
                .orElseThrow(() -> {
                    String message = "Region with id:" + id + " not found";
                    throw new RegionNotFoundException(message);
                });
    }

    @Override
    public List<RegionDTO> all(String countryName) {
        String clearCountryName = checkAndNormalizeCountry(countryName);
        return regionRepository.findAllRegionDTOByCountryNameIgnoringCase(clearCountryName);
    }

    @Override
    @Transactional
    public RegionDTO rename(String countryName, String oldName, String newName) {
        String clearCountryName = checkAndNormalizeCountry(countryName);
        String clearOldRegionName = checkAndNormalizeRegion(oldName);
        String clearNewRegionName = checkAndNormalizeRegion(newName);
        RegionDTO region = findRegionInCountryByNames(clearCountryName, clearOldRegionName);
        regionRepository.updateNameOfRegionById(region.getId(), clearNewRegionName);
        return region.toBuilder().name(clearNewRegionName).build();
    }

    @Override
    @Transactional
    public RegionDTO rename(Long id, String newName) {
        String clearNewRegionName = checkAndNormalizeRegion(newName);
        RegionDTO region = get(id);
        regionRepository.updateNameOfRegionById(region.getId(), clearNewRegionName);
        return region.toBuilder().name(clearNewRegionName).build();
    }

    @Override
    public RegionDTO delete(String countryName, String name) {
        String clearCountryName = checkAndNormalizeCountry(countryName);
        String clearRegionName = checkAndNormalizeRegion(name);
        RegionDTO regionDTO = findRegionInCountryByNames(clearCountryName, clearRegionName);
        Country country = countryRepository.findCountryByName(clearCountryName).orElseThrow(CountryNotFoundException::new);
//        StatisticResult result = statistic.deleteChild(new CountryDTO(country), regionDTO);
        regionRepository.deleteById(regionDTO.getId());
        return regionDTO;
    }

    @Override
    public RegionDTO delete(Long id) {
        RegionDTO regionDTO = get(id);
        regionRepository.deleteById(regionDTO.getId());
        return regionDTO;
    }


    @Override
    public List<RegionDTO> deleteAllByCountry(String country) {
        String clearCountryName = checkAndNormalizeCountry(country);
        if (!countryRepository.existsCountryByNameIgnoreCase(clearCountryName)) {
            throw new CountryNotFoundException("Country " + country + " doesn't exist");
        }
        List<RegionDTO> regions = regionRepository.findAllRegionDTOByCountryNameIgnoringCase(country);
        regions.forEach(r -> regionRepository.deleteById(r.getId()));
        countryRepository.updateMoneyOfCountryByNameIgnoreCase(country, BigDecimal.ZERO);
        return regions;
    }

}
