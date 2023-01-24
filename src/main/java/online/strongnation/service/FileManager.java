package online.strongnation.service;

import java.io.File;

public interface FileManager {
    void save(String path, File file);
    File read(String path);
    void delete(String path);
}
