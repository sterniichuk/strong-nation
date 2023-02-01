package online.strongnation.repository;

import online.strongnation.model.entity.CategoryDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryDAO, Long> {
}
