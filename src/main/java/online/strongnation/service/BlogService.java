package online.strongnation.service;

import online.strongnation.dto.BlogDTO;
import online.strongnation.dto.GetBlogResponse;

import java.util.List;

public interface BlogService {
    Long addNewBlogByRegionId(Long id, BlogDTO blogDTO);//returns id of created blog
    Long addNewBlogByRegionName(String name, BlogDTO blogDTO);

    List<GetBlogResponse> findAllByRegionId(Long id);
    List<GetBlogResponse> findAllByRegionName(String id);

    void updateBlogByRegionId(BlogDTO blogDTO);
    void updateBlogByRegionName(BlogDTO blogDTO);

    void updateBlogById(BlogDTO blogDTO);
}
