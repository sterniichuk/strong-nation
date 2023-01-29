package online.strongnation.repository;

import online.strongnation.model.dto.CountryDTO;
import online.strongnation.model.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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

    @Modifying
    @Query("update Country c set c.money = :money where lower(c.name) = lower(:name)")
    void updateMoneyOfCountryByNameIgnoreCase(@Param("name") String name, @Param("money") BigDecimal money);

    @Query("select new online.strongnation.model.dto.CountryDTO(c)" +
            " from Country c where upper(c.name) = upper(:name)")
    Optional<CountryDTO> findCountryDTOByNameIgnoreCase(String name);

    @Query("select new online.strongnation.model.dto.CountryDTO(c) from Country c")
    List<CountryDTO> findAllDTO();
}