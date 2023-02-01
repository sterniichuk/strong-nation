package online.strongnation.model.dto;

import lombok.*;
import online.strongnation.model.BigDecimalEquals;
import online.strongnation.model.entity.Post;
import online.strongnation.model.entity.PostCategory;
import online.strongnation.model.statistic.StatisticModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Builder(toBuilder = true)
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO implements StatisticModel {
    private Long id;
    private String heading;
    private String link;
    private BigDecimal money;
    private LocalDateTime date;
    private List<CategoryDTO> categories;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostDTO postDTO = (PostDTO) o;
        return Objects.equals(id, postDTO.id) && heading.equals(postDTO.heading) && link.equals(postDTO.link) && BigDecimalEquals.compare(money, postDTO.money) && date.equals(postDTO.date) && Objects.equals(categories, postDTO.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, heading, link, money, date, categories);
    }

    public PostDTO(Post post) {
        this.id = post.getId();
        this.heading = post.getHeading();
        this.link = post.getLink();
        this.money = post.getMoney();
        this.date = post.getDate();
        setPostCategories(post.getCategories());
    }

    public void setPostCategories(List<PostCategory> list){
        this.categories = list.stream().map(PostCategory::getCategoryDAO).map(CategoryDTO::new).toList();
    }

    public GetPostResponse toGetResponse(){
        return GetPostResponse.builder()
                .id(id)
                .date(date)
                .heading(heading)
                .link(link)
                .build();
    }

    public PostDTO getWithId(Long id){
        return PostDTO.builder()
                .id(id)
                .date(date)
                .money(money)
                .heading(heading)
                .link(link)
                .categories(categories)
                .build();
    }
}
