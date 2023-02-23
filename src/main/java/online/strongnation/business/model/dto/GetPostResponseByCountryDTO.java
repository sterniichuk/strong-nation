package online.strongnation.business.model.dto;

import lombok.*;
import online.strongnation.business.model.entity.Post;

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

    private Boolean important;

    private String region;

    public GetPostResponseByCountryDTO(Post post) {
        this.id = post.getId();
        this.description = post.getDescription();
        this.link = post.getLink();
        this.date = post.getDate();
        this.important = post.getImportant();
        this.region = post.getRegion().getName();
    }
}
