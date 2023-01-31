package online.strongnation.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Long id;
    private String heading;
    private String link;
    private BigDecimal money;
    private LocalDateTime date;
    private List<CategoryDTO> categories;

    public GetPostResponse toGetResponse(){
        return GetPostResponse.builder()
                .id(id)
                .date(date)
                .heading(heading)
                .link(link)
                .build();
    }
}
