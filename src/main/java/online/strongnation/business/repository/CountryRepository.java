package online.strongnation.business.repository;

import online.strongnation.business.model.dto.CountryDTO;
import online.strongnation.business.model.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findCountryByName(String name);

    Optional<Country> findCountryByNameIgnoreCase(String name);

    boolean existsCountryByNameIgnoreCase(String name);

    @Query("select c.id from Country c where upper(c.name) = upper(:name)")
    Optional<Long> getIdByNameIgnoreCase(String name);

    @Modifying
    @Query("update Country c set c.name = :new where lower(c.name) = lower(:old)")
    void updateNameOfCountry(@Param("old") String oldName, @Param("new") String newName);

    @Query("select new online.strongnation.business.model.dto.CountryDTO(c)" +
            " from Country c where upper(c.name) = upper(:name)")
    Optional<CountryDTO> findCountryDTOByNameIgnoreCase(String name);

    @Query("select new online.strongnation.business.model.dto.CountryDTO(c) from Country c")
    List<CountryDTO> findAllDTO();
}