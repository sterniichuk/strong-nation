package online.strongnation.model.dto;

import lombok.*;
import online.strongnation.model.entity.Post;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class GetPostResponseByCountryDTO {
    private Long id;
    private String description;
    private String link;
    private LocalDateTime date;
    private String region;

    public GetPostResponseByCountryDTO(Post post) {
        this.id = post.getId();
        this.description = post.getDescription();
        this.link = post.getLink();
        this.date = post.getDate();
        this.region = post.getRegion().getName();
    }
}
