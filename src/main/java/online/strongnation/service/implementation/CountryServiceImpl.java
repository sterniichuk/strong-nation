package online.strongnation.service.implementation;

import lombok.RequiredArgsConstructor;
import online.strongnation.model.dto.CountryDTO;
import online.strongnation.model.entity.Country;
import online.strongnation.exception.CountryNotFoundException;
import online.strongnation.exception.IllegalCountryException;
import online.strongnation.repository.CountryRepository;
import online.strongnation.service.CountryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static online.strongnation.service.implementation.RequestParameterFixer.checkAndNormalizeCountry;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Override
    public CountryDTO create(final String name) {
        String clearName = checkAndNormalizeCountry(name);
        if (countryRepository.existsCountryByNameIgnoreCase(clearName)) {
            throw new IllegalCountryException("Country " + name + " already exists");
        }
        Country country = new Country(clearName);
        countryRepository.save(country);
        return new CountryDTO(country);
    }

    @Override
    public CountryDTO get(final String name) {
        final String clearName = checkAndNormalizeCountry(name);
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
        final String oldNameClear = checkAndNormalizeCountry(oldName);
        final String newNameClear = checkAndNormalizeCountry(newName);
        if (!countryRepository.existsCountryByNameIgnoreCase(oldNameClear)) {
            throw new CountryNotFoundException("Country " + oldNameClear + " not found");
        }
        countryRepository.updateNameOfCountry(oldName, newNameClear);
        return getByNormalizedName(newNameClear);
    }

    @Override
    public CountryDTO delete(String name) {
        final String clearName = checkAndNormalizeCountry(name);
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
