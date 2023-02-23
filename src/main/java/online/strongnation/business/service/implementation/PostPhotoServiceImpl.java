package online.strongnation.business.service.implementation;

import online.strongnation.business.config.Constants;
import online.strongnation.business.exception.PostNotFoundException;
import online.strongnation.business.exception.PostPhotoNotFoundException;
import online.strongnation.business.model.entity.Post;
import online.strongnation.business.model.entity.PostPhoto;
import online.strongnation.business.repository.PostPhotoRepository;
import online.strongnation.business.repository.PostRepository;
import online.strongnation.business.service.FileService;
import online.strongnation.business.service.PostPhotoService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static online.strongnation.business.service.implementation.FileUtils.getUnifiedPhotoFileName;
import static online.strongnation.business.service.implementation.FileUtils.initFolder;

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
        initPostPhotoDirectory();
    }

    private void initPostPhotoDirectory() {
        final String message = "Can't create folder for saving post's photo";
        initFolder(getPhotoDirectory(), message);
    }

    private String getPhotoDirectory() {
        return constants.PATH_TO_PHOTO_DIRECTORY +
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
    public String deletePhotoByPostPhoto(PostPhoto postPhoto) {
        String oldPath = getAbsolutePhotoPath(postPhoto);
        fileService.delete(oldPath);
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



    @Override
    public Resource downloadPhotoByPostId(Long id) {
        String path;
        try{
            PostPhoto postPhoto = getPostPhoto(id);
            path = getAbsolutePhotoPath(postPhoto);
        }catch (PostPhotoNotFoundException e){
            path = constants.PATH_TO_DEFAULT_POST_PHOTO;
        }
        return fileService.read(path);
    }

    @Override
    public List<String> deleteAll() {
        List<PostPhoto> all = postPhotoRepository.findAll();
        List<String> list = new LinkedList<>();
        all.forEach(x -> {
            fileService.delete(getAbsolutePhotoPath(x));
            list.add(x.getRelativePathToPhoto());
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
