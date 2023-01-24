package online.strongnation.service;

import online.strongnation.dto.CategoryDTO;
import online.strongnation.dto.RegionDTO;

import java.util.List;

public interface RegionService {
    void addNewRegion(String name);

    List<RegionDTO> findAll();
    RegionDTO findById(Long id);
    RegionDTO findByName(String countryName, String name);

    void hardUpdateRegionById(Long id, RegionDTO region);
    void hardUpdateRegionByName(String oldName, RegionDTO region);
    void hardAddNewCategoryById(Long id, CategoryDTO category);
    void hardAddNewCategoryName(String name, CategoryDTO category);

    void deleteRegionById(Long id);
    void deleteRegionByName(String name);
}
