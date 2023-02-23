package online.strongnation.business.service.implementation;

import online.strongnation.business.exception.IllegalFileServiceException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public interface FileUtils {
    static void initFolder(String photoDirectory, String message) {
        try {
            Files.createDirectories(Paths.get(photoDirectory));
        } catch (IOException e) {
            throw new IllegalFileServiceException(message, e);
        }
    }

    static <T> String getUnifiedPhotoFileName(T id, MultipartFile file) {
        final String oldName = file.getOriginalFilename();
        final String extension = StringUtils.getFilenameExtension(oldName);
        return id + "." + extension;
    }
}
