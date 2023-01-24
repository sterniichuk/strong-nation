package online.strongnation.service.implementation;

import online.strongnation.dto.RegionDTO;
import online.strongnation.service.RegionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionServiceImpl implements RegionService {

    @Override
    public RegionDTO create(String countryName, String name) {
        return null;
    }

    @Override
    public List<RegionDTO> createAll(String countryName, List<String> names) {
        return null;
    }

    @Override
    public RegionDTO get(String countryName, String name) {
        return null;
    }

    @Override
    public RegionDTO get(Long id) {
        return null;
    }

    @Override
    public List<RegionDTO> all(String countryName) {
        return null;
    }

    @Override
    public RegionDTO rename(String countryName, String oldName, String newName) {
        return null;
    }

    @Override
    public RegionDTO renameById(Long id, String newName) {
        return null;
    }

    @Override
    public RegionDTO delete(String countryName, String name) {
        return null;
    }

    @Override
    public RegionDTO deleteById(Long id) {
        return null;
    }

    @Override
    public List<RegionDTO> deleteAll() {
        return null;
    }
}
