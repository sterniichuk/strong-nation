package online.strongnation.repository;

import online.strongnation.entity.Country;
import online.strongnation.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

public interface CountryRepository extends JpaRepository<Country, Long> {
}