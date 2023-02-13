package online.strongnation.model.dto;

import lombok.*;
import online.strongnation.model.entity.Post;
import online.strongnation.model.entity.PostCategory;
import online.strongnation.model.statistic.StatisticModel;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO implements StatisticModel {
    private Long id;
    private String region;
    private String description;
    private String link;
    private LocalDateTime date;
    private List<CategoryDTO> categories;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.region = post.getRegion().getName();
        this.description = post.getDescription();
        this.link = post.getLink();
        this.date = post.getDate();
        setPostCategories(post.getCategories());
    }

    public void setPostCategories(List<PostCategory> list) {
        this.categories = list.stream().map(PostCategory::getCategoryDAO).map(CategoryDTO::new).toList();
    }

    public GetPostResponse toGetResponse() {
        return GetPostResponse.builder()
                .id(id)
                .date(date)
                .description(description)
                .link(link)
                .build();
    }

    public GetPostResponseByCountryDTO toGetResponseByCountry(String region) {
        return GetPostResponseByCountryDTO.builder()
                .id(id)
                .region(region)
                .date(date)
                .description(description)
                .link(link)
                .build();
    }

    public PostDTO getWithId(Long id) {
        return PostDTO.builder()
                .id(id)
                .date(date)
                .description(description)
                .link(link)
                .region(region)
                .categories(categories)
                .build();
    }
}
