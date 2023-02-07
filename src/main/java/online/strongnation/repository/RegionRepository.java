package online.strongnation.repository;

import online.strongnation.model.dto.RegionDTO;
import online.strongnation.model.entity.Country;
import online.strongnation.model.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findFirstByName(String name);

    boolean existsRegionByName(String name);

    @Query("SELECT COUNT(reg)>0 FROM Region reg JOIN reg.country c" +
            " WHERE LOWER(reg.name) = LOWER(:region) AND LOWER(c.name)=LOWER(:country)")
    boolean existsRegionInCountryByNamesIgnoringCase(String country, String region);

    @Query("SELECT new online.strongnation.model.dto.RegionDTO(reg)" +
            " FROM Region reg JOIN reg.country c " +
            "WHERE UPPER(c.name) = UPPER(:country) AND UPPER(reg.name) = UPPER(:region)")
    Optional<RegionDTO> findRegionDTOInCountryByNamesIgnoringCase(String country, String region);

    @Query("SELECT reg FROM Region reg JOIN reg.country c " +
            "WHERE UPPER(c.name) = UPPER(:country) AND UPPER(reg.name) = UPPER(:region)")
    Optional<Region> findRegionInCountryByNamesIgnoringCase(String country, String region);

    @Modifying
    @Query("update Region reg set reg.name = :newName where reg.id = :id")
    void updateNameOfRegionById(Long id, String newName);

    @Query("SELECT new online.strongnation.model.dto.RegionDTO(reg)" +
            " FROM Region reg WHERE reg.id = :id")
    Optional<RegionDTO> findRegionDTOById(Long id);

    @Query("SELECT new online.strongnation.model.dto.RegionDTO(reg)" +
            " FROM Region reg JOIN reg.country c " +
            "WHERE UPPER(c.name) = UPPER(:country)")
    List<RegionDTO> findAllRegionDTOByCountryNameIgnoringCase(String country);

    @Query("SELECT c FROM Region reg JOIN reg.country c WHERE reg.id = :id")
    Optional<Country> findCountryOfRegionById(Long id);
}
