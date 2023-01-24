package online.strongnation.service;

import online.strongnation.dto.PostDTO;
import online.strongnation.dto.GetPostResponse;

import java.util.List;

public interface PostService {
    Long addNewPostByRegionId(Long id, PostDTO postDTO);//returns id of created blog
    Long addNewPostByRegionName(String name, PostDTO postDTO);

    List<GetPostResponse> findAllByRegionId(Long id);
    List<GetPostResponse> findAllByRegionName(String id);

    void updatePostByRegionId(PostDTO postDTO);
    void updatePostByRegionName(PostDTO postDTO);

    void updatePostById(PostDTO postDTO);
}
