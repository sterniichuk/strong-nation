package online.strongnation.unit.repository;

import online.strongnation.business.model.entity.SliderPhoto;
import online.strongnation.business.repository.SliderPhotoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class SliderPhotoRepositoryTest {

    @Autowired
    private SliderPhotoRepository sliderPhotoRepository;

    @AfterEach
    void tearDown() {
        sliderPhotoRepository.deleteAll();
    }

    @Test
    void findIdsOfSliderPhotos() {
        List<SliderPhoto> entities = List.of(
                new SliderPhoto("path1"),
                new SliderPhoto("path2"),
                new SliderPhoto("path3"),
                new SliderPhoto("path4"),
                new SliderPhoto("path5")
        );
        sliderPhotoRepository.saveAll(entities);
        List<Long> idsOfSliderPhotos = sliderPhotoRepository.findIdsOfSliderPhotos();
        assertThat(idsOfSliderPhotos.size()).isEqualTo(entities.size());
    }
}