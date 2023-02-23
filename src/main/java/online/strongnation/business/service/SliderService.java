package online.strongnation.business.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SliderService {
    Long uploadSliderPhoto(MultipartFile file);
    List<Long> getListOfSliderPhotoIds();

    Resource downloadSliderPhotoById(Long id);

    Long deleteSliderPhotoById(Long id);
}
