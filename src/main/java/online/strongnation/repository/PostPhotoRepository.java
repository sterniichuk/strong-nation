package online.strongnation.repository;

import online.strongnation.entity.PostPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostPhotoRepository extends JpaRepository<PostPhoto, Long> {
}