package online.strongnation.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class GetBlogResponse {
    private Long id;
    private String heading;
    private String link;
    private LocalDateTime date;
}
