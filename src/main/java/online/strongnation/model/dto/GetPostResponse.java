package online.strongnation.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class GetPostResponse {
    private Long id;
    private String heading;
    private String link;
    private LocalDateTime date;
}
