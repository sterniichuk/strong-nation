package online.strongnation.service.implementation;

import online.strongnation.dto.CategoryDTO;
import online.strongnation.dto.RegionDTO;
import online.strongnation.service.RegionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionServiceImpl implements RegionService {
    @Override
    public void addNewRegion(String name) {
    }

    @Override
    public List<RegionDTO> findAll() {return null;}

    @Override
    public RegionDTO findById(Long id) {return null;}

    @Override
    public RegionDTO findByName(String countryName, String name) {
        return null;
    }

    @Override
    public void hardUpdateRegionById(Long id, RegionDTO region) {}

    @Override
    public void hardUpdateRegionByName(String oldName, RegionDTO region) {}

    @Override
    public void hardAddNewCategoryById(Long id, CategoryDTO category) {}

    @Override
    public void hardAddNewCategoryName(String name, CategoryDTO category) {}

    @Override
    public void deleteRegionById(Long id) {}

    @Override
    public void deleteRegionByName(String name) {}
}
