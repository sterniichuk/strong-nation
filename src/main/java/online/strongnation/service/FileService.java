package online.strongnation.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void save(String path, MultipartFile file);

    Resource read(String path);

    void delete(String path);
}
