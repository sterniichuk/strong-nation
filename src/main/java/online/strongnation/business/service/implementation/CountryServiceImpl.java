package online.strongnation.business.service.implementation;

import lombok.AllArgsConstructor;
import online.strongnation.business.exception.CountryNotFoundException;
import online.strongnation.business.exception.IllegalCountryException;
import online.strongnation.business.repository.CountryRepository;
import online.strongnation.business.service.CountryService;
import online.strongnation.business.service.PostPhotoService;
import online.strongnation.business.model.dto.CountryDTO;
import online.strongnation.business.model.entity.Country;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static online.strongnation.business.service.implementation.RequestParameterFixer.checkAndNormalizeCountry;

@Service
@AllArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final PostPhotoService postPhotoService;

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
        postPhotoService.deletePhotoCountryId(deleted.getId());
        countryRepository.deleteById(deleted.getId());
        return deleted;
    }

    @Override
    public List<CountryDTO> deleteAll() {
        final var all = countryRepository.findAllDTO();
        postPhotoService.deleteAll();
        countryRepository.deleteAll();
        return all;
    }
}
