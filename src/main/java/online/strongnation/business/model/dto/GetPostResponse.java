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
public class GetPostResponse {
    private Long id;
    private String description;
    private String link;
    private LocalDateTime date;

    public GetPostResponse(Post post) {
        this.id = post.getId();
        this.description = post.getDescription();
        this.link = post.getLink();
        this.date = post.getDate();
    }
}
