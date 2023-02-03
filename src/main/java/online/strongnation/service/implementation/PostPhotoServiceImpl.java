package online.strongnation.service.implementation;

import online.strongnation.config.Constants;
import online.strongnation.exception.IllegalFileServiceException;
import online.strongnation.exception.PostNotFoundException;
import online.strongnation.exception.PostPhotoNotFoundException;
import online.strongnation.model.entity.Post;
import online.strongnation.model.entity.PostPhoto;
import online.strongnation.repository.PostPhotoRepository;
import online.strongnation.repository.PostRepository;
import online.strongnation.service.FileService;
import online.strongnation.service.PostPhotoService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

@Service
public class PostPhotoServiceImpl implements PostPhotoService {

    private final Constants constants;
    private final FileService fileService;

    private static final String $ = File.separator;
    private final PostPhotoRepository postPhotoRepository;
    private final PostRepository postRepository;

    public PostPhotoServiceImpl(Constants constants, FileService fileService, PostPhotoRepository postPhotoRepository, PostRepository postRepository) {
        this.constants = constants;
        this.fileService = fileService;
        this.postPhotoRepository = postPhotoRepository;
        this.postRepository = postRepository;
        initPostPhotoFolder();
    }

    private void initPostPhotoFolder() {
        try {
            Files.createDirectories(Paths.get(getPhotoDirectory()));
        } catch (IOException e) {
            String message = "Can't create folder for saving post's photo";
            throw new IllegalFileServiceException(message, e);
        }
    }

    private String getPhotoDirectory() {
        return constants.PATH_TO_POST_PHOTO_DIRECTORY +
                $ + constants.POST_PHOTO_DIRECTORY_NAME;
    }

    @Override
    public Long uploadPhotoByPostId(Long id, MultipartFile file) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> {
                    String message = "There is no post with id: " + id + ". Photo is not saved";
                    throw new PostNotFoundException(message);
                });
        PostPhoto postPhoto = post.getPostPhoto();
        if (postPhoto != null) {
            String oldPath = getAbsolutePhotoPath(postPhoto);
            post.setPostPhoto(null);
            fileService.delete(oldPath);
            postPhotoRepository.deleteById(postPhoto.getId());
        }
        String newName = getUnifiedPhotoFileName(id, file);
        String newPath = getAbsolutePhotoPath(newName);
        fileService.save(newPath, file);
        post.setPostPhoto(new PostPhoto(newName));
        postRepository.save(post);
        return id;
    }

    @Override
    public String deletePhotoByPostId(Long id) {
        PostPhoto postPhoto = getPostPhoto(id);
        return deletePhotoByPostPhoto(postPhoto);
    }

    @Override
    public String deletePhotoByPostPhoto(PostPhoto postPhoto) {
        String oldPath = getAbsolutePhotoPath(postPhoto);
        fileService.delete(oldPath);
//        postPhotoRepository.delete(postPhoto);
        return postPhoto.getRelativePathToPhoto();
    }

    @Override
    public List<String> deletePhotoCountryId(Long id) {
        List<String> list = new LinkedList<>();
        postPhotoRepository.findAllPostPhotoByCountryId(id).forEach(p -> {
            String s = deletePhotoByPostPhoto(p);
            list.add(s);
        });
        return list;
    }

    @Override
    public List<String> deletePhotoByRegionId(Long id) {
        List<String> list = new LinkedList<>();
        postPhotoRepository.findAllPostPhotoByRegionId(id).forEach(p -> {
            String s = deletePhotoByPostPhoto(p);
            list.add(s);
        });
        return list;
    }

    private PostPhoto getPostPhoto(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> {
                    String message = "There is no post with id: " + id + ". Photo is not saved";
                    throw new PostNotFoundException(message);
                });
        PostPhoto postPhoto = post.getPostPhoto();
        if (postPhoto == null) {
            String message = "Post with id: " + id + " doesn't have any photo yet";
            throw new PostPhotoNotFoundException(message);
        }
        return postPhoto;
    }

    private String getUnifiedPhotoFileName(Long id, MultipartFile file) {
        final String oldName = file.getOriginalFilename();
        final String extension = StringUtils.getFilenameExtension(oldName);
        return id + "." + extension;
    }

    @Override
    public Resource downloadPhotoByPostId(Long id) {
        PostPhoto postPhoto = getPostPhoto(id);
        String path = getAbsolutePhotoPath(postPhoto);
        return fileService.read(path);
    }

    @Override
    public List<String> deleteAll() {
        List<PostPhoto> all = postPhotoRepository.findAll();
        List<String> list = new LinkedList<>();
        all.forEach(x -> {
            fileService.delete(getAbsolutePhotoPath(x));
            list.add(x.getRelativePathToPhoto());
//            postPhotoRepository.delete(x);
        });
        return list;
    }

    private String getAbsolutePhotoPath(PostPhoto x) {
        return getAbsolutePhotoPath(x.getRelativePathToPhoto());
    }

    private String getAbsolutePhotoPath(String fileName) {
        return getPhotoDirectory() + $ + fileName;
    }
}
