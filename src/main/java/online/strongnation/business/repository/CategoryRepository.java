package online.strongnation.business.repository;

import online.strongnation.business.model.entity.CategoryDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryDAO, Long> {
}
