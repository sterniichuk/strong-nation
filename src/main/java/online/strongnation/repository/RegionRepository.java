package online.strongnation.repository;

import online.strongnation.model.dto.RegionDTO;
import online.strongnation.model.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findFirstByName(String name);

    boolean existsRegionByName(String name);

    @Query("SELECT COUNT(reg)>0 FROM Country c JOIN c.regions reg" +
            " WHERE LOWER(reg.name) = LOWER(:region) AND LOWER(c.name)=LOWER(:country)")
    boolean existsRegionInCountryByNamesIgnoringCase(String country, String region);

    @Query("SELECT new online.strongnation.model.dto.RegionDTO(reg)" +
            " FROM Country c JOIN c.regions reg " +
            "WHERE UPPER(c.name) = UPPER(:country) AND UPPER(reg.name) = UPPER(:region)")
    Optional<RegionDTO> findRegionDTOInCountryByNamesIgnoringCase(String country, String region);

    @Query("SELECT new online.strongnation.model.dto.RegionDTO(reg)" +
            " FROM Country c JOIN c.regions reg " +
            "WHERE UPPER(c.name) = UPPER(:country)")
    List<RegionDTO> findAllRegionDTOByCountryNameIgnoringCase(String country);
}
