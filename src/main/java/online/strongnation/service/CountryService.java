package online.strongnation.service;

import online.strongnation.dto.CountryDTO;

import java.util.List;

public interface CountryService {
    CountryDTO create(String name);
    CountryDTO get(String name);
    List<CountryDTO> getAll();
    CountryDTO rename(String oldName, String newName);
    CountryDTO delete(String name);
    List<CountryDTO> deleteAll();
}
