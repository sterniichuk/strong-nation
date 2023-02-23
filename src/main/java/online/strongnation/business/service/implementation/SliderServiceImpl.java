package online.strongnation.business.service.implementation;

import online.strongnation.business.config.Constants;
import online.strongnation.business.exception.SliderPhotoNotFoundException;
import online.strongnation.business.model.entity.SliderPhoto;
import online.strongnation.business.repository.SliderPhotoRepository;
import online.strongnation.business.service.FileService;
import online.strongnation.business.service.SliderService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import static online.strongnation.business.service.implementation.FileUtils.getUnifiedPhotoFileName;
import static online.strongnation.business.service.implementation.FileUtils.initFolder;

@Service
public class SliderServiceImpl implements SliderService {

    private final Constants constants;
    private final FileService fileService;

    private static final String $ = File.separator;
    private final SliderPhotoRepository sliderPhotoRepository;

    public SliderServiceImpl(Constants constants, FileService fileService, SliderPhotoRepository sliderPhotoRepository) {
        this.constants = constants;
        this.fileService = fileService;
        this.sliderPhotoRepository = sliderPhotoRepository;
        initSliderPhotoDirectory();
    }

    private void initSliderPhotoDirectory() {
        final String message = "Can't create folder for saving slider's photo";
        initFolder(getPhotoDirectory(), message);
    }

    private String getPhotoDirectory() {
        return constants.PATH_TO_PHOTO_DIRECTORY + $ + constants.SLIDER_PHOTO_DIRECTORY_NAME;
    }

    private String getAbsolutePhotoPath(String fileName) {
        return getPhotoDirectory() + $ + fileName;
    }

    @Override
    public Long uploadSliderPhoto(MultipartFile file) {
        String newName = getUnifiedPhotoFileName(LocalDateTime.now().toString().replace(':','_'), file);
        String newPath = getAbsolutePhotoPath(newName);
        fileService.save(newPath, file);
        SliderPhoto photo = new SliderPhoto();
        photo.setRelativePathToPhoto(newName);
        sliderPhotoRepository.save(photo);
        return photo.getId();
    }

    @Override
    public List<Long> getListOfSliderPhotoIds() {
        return sliderPhotoRepository.findIdsOfSliderPhotos();
    }

    @Override
    public Resource downloadSliderPhotoById(Long id) {
        String path;
        try {
            SliderPhoto sliderPhoto = getSliderPhoto(id);
            path = getAbsolutePhotoPath(sliderPhoto.getRelativePathToPhoto());
        } catch (SliderPhotoNotFoundException e) {
            path = constants.PATH_TO_DEFAULT_POST_PHOTO;
        }
        return fileService.read(path);
    }

    private SliderPhoto getSliderPhoto(Long id) {
        return sliderPhotoRepository.findById(id).orElseThrow(() ->{
            throw new SliderPhotoNotFoundException("There is no slider photo with id: " + id);
        });
    }

    @Override
    public Long deleteSliderPhotoById(Long id) {
        SliderPhoto photo = getSliderPhoto(id);
        String oldPath = getAbsolutePhotoPath(photo.getRelativePathToPhoto());
        fileService.delete(oldPath);
        sliderPhotoRepository.deleteById(id);
        return id;
    }
}
