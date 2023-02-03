package online.strongnation.service.implementation;

import online.strongnation.exception.IllegalFileServiceException;
import online.strongnation.service.FileService;
import org.jboss.logging.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {
    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public void save(String path, MultipartFile file) {
        try {
            file.transferTo(Path.of(path));
        } catch (IOException e) {
            String message = "Failed to transfer multipart file to filesystem";
            throw new IllegalFileServiceException(message, e);
        }
    }

    @Override
    public Resource read(String path) {
        return loadAsResource(path);
    }

    @Override
    public void delete(String path) {
        Path pathObj = Paths.get(path);
        try {
            boolean exists = Files.deleteIfExists(pathObj);
            if (!exists) {
                logger.info("file: " + pathObj.getFileName() + " doesn't exist");
            }
        } catch (IOException e) {
            throw new IllegalFileServiceException("Failed to delete file " + pathObj.getFileName(), e);
        }
    }

    private Resource loadAsResource(String path) {
        Path file = Path.of(path);
        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            String message = "file: " + file.getFileName()
                    + " doesn't exist or file is not readable";
            throw new IllegalFileServiceException(message);
        } catch (IOException e) {
            throw new IllegalFileServiceException("Could not read file: " + file.getFileName(), e);
        }
    }
}
