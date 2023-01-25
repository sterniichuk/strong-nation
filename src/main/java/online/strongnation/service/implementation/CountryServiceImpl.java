package online.strongnation.service.implementation;

import lombok.RequiredArgsConstructor;
import online.strongnation.config.EntityNameLength;
import online.strongnation.dto.CountryDTO;
import online.strongnation.entity.Country;
import online.strongnation.exception.CountryNotFoundException;
import online.strongnation.exception.IllegalCountryException;
import online.strongnation.repository.CountryRepository;
import online.strongnation.service.CountryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Override
    public CountryDTO create(final String name) {
        String clearName = checkAndNormalize(name);
        if (countryRepository.existsCountryByNameIgnoreCase(clearName)) {
            throw new IllegalCountryException("Country " + name + " already exists");
        }
        Country country = new Country(clearName);
        countryRepository.save(country);
        return new CountryDTO(country);
    }

    private String checkAndNormalize(final String name) {
        if (name == null) {
            throw new IllegalCountryException("Name of country is null");
        }
        if (name.length() >= EntityNameLength.COUNTRY.length) {
            throw new IllegalCountryException("Too long name of country");
        }
        String clearName = StringUtils.normalizeSpace(name);
        if (clearName.length() == 0) {
            throw new IllegalCountryException("Empty name");
        }
        return clearName;
    }

    @Override
    public CountryDTO get(final String name) {
        final String clearName = checkAndNormalize(name);
        return getByNormalizedName(clearName);
    }

    private CountryDTO getByNormalizedName(final String clearName) {
        return countryRepository.findCountryDTOByNameIgnoreCase(clearName)
                .orElseThrow(() -> {
                    throw new CountryNotFoundException("Country " + clearName + " doesn't exist");
                });
    }

    @Override
    public List<CountryDTO> getAll() {
        return countryRepository.findAllDTO();
    }

    @Override
    @Transactional
    public CountryDTO rename(String oldName, String newName) {
        final String oldNameClear = checkAndNormalize(oldName);
        final String newNameClear = checkAndNormalize(newName);
        if (!countryRepository.existsCountryByNameIgnoreCase(oldNameClear)) {
            throw new CountryNotFoundException("Country " + oldNameClear + " not found");
        }
        countryRepository.updateNameOfCountry(oldName, newNameClear);
        return getByNormalizedName(newNameClear);
    }

    @Override
    public CountryDTO delete(String name) {
        final String clearName = checkAndNormalize(name);
        final CountryDTO deleted = getByNormalizedName(clearName);
        countryRepository.deleteById(deleted.getId());
        return deleted;
    }

    @Override
    public List<CountryDTO> deleteAll() {
        final var all = countryRepository.findAllDTO();
        countryRepository.deleteAll();
        return all;
    }
}
