package online.strongnation.repository;

import online.strongnation.entity.BlogPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPhotoRepository extends JpaRepository<BlogPhoto, Long> {
}