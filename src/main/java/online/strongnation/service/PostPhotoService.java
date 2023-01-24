package online.strongnation.service;

import org.springframework.web.multipart.MultipartFile;

public interface PostPhotoService {
    void uploadPhotoByBlogId(Long id, MultipartFile file); //post's id
    void downloadPhotoById(Long id); //post's id
}
