package online.strongnation.business.model.dto;

import lombok.*;
import online.strongnation.business.model.entity.Post;
import online.strongnation.business.model.entity.PostCategory;
import online.strongnation.business.model.statistic.StatisticModel;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO implements StatisticModel<RegionDTO> {
    private Long id;
    private String region;
    private String description;
    private String link;
    private LocalDateTime date;
    private Boolean important;
    private List<CategoryDTO> categories;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.region = post.getRegion().getName();
        this.description = post.getDescription();
        this.link = post.getLink();
        this.date = post.getDate();
        this.important = post.getImportant();
        setPostCategories(post.getCategories());
    }

    public void setPostCategories(List<PostCategory> list) {
        this.categories = list.stream().map(PostCategory::getCategoryEntity).map(CategoryDTO::new).toList();
    }

    public GetPostResponse toGetResponse() {
        return GetPostResponse.builder()
                .id(id)
                .date(date)
                .description(description)
                .link(link)
                .important(important)
                .build();
    }

    public GetPostResponseByCountry toGetResponseByCountry(String region) {
        return GetPostResponseByCountry.builder()
                .id(id)
                .region(region)
                .date(date)
                .description(description)
                .important(important)
                .link(link)
                .build();
    }

    public PostDTO getWithId(Long id) {
        return this.toBuilder().id(id).build();
    }
}
