package online.strongnation.business.service;

import online.strongnation.business.model.dto.GetPostResponse;
import online.strongnation.business.model.dto.GetPostResponseByCountryDTO;
import online.strongnation.business.model.dto.PostDTO;

import java.util.List;

public interface PostService {
    PostDTO create(PostDTO post, String countryName, String region);

    PostDTO create(PostDTO post, Long id);

    List<GetPostResponse> all(String countryName, String regionName);

    List<GetPostResponseByCountryDTO> all(String countryName);

    List<GetPostResponse> all(Long id);//regionId

    PostDTO get(Long id);

    PostDTO update(PostDTO post);

    PostDTO delete(Long id);

    List<PostDTO> deleteAllByRegionId(Long id);

    List<PostDTO> deleteAll(String countryName, String region);

    Boolean setImportant(Long id, Boolean important);
}
