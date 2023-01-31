package online.strongnation.service.implementation;

import online.strongnation.model.dto.GetPostResponse;
import online.strongnation.model.dto.PostDTO;
import online.strongnation.service.PostService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Override
    public PostDTO create(PostDTO post, String countryName, String region) {
        return null;
    }

    @Override
    public PostDTO create(PostDTO post, Long id) {
        return null;
    }

    @Override
    public List<GetPostResponse> all(String countryName, String regionName) {
        return null;
    }

    @Override
    public List<GetPostResponse> all(Long id) {
        return null;
    }

    @Override
    public PostDTO get(Long id) {
        return null;
    }

    @Override
    public PostDTO update(PostDTO post) {
        return null;
    }

    @Override
    public PostDTO delete(Long id) {
        return null;
    }

    @Override
    public List<PostDTO> deleteAllByRegionId(Long id) {
        return null;
    }

    @Override
    public List<PostDTO> deleteAll(String countryName, String region) {
        return null;
    }
}
