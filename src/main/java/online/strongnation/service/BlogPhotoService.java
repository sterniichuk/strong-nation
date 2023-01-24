package online.strongnation.service;

import java.io.File;

public interface BlogPhotoService {
    void addPhotoByBlogId(Long id, File file); //blog's id
    File getPhotoById(Long id);//photo's id
    void updatePhotoById(Long id, File file); //photo's id
    void deletePhotoById(Long id); //photo's id
}
