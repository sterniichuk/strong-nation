package online.strongnation.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class GetPostResponse {
    private Long id;
    private String heading;
    private String link;
    private LocalDateTime date;
}
