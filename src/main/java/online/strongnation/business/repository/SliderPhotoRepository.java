package online.strongnation.business.repository;

import online.strongnation.business.model.entity.SliderPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SliderPhotoRepository extends JpaRepository<SliderPhoto, Long> {

    @Query("SELECT p.id FROM SliderPhoto p")
    List<Long> findIdsOfSliderPhotos();
}
