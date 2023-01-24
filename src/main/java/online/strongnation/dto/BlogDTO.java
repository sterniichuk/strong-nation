package online.strongnation.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class BlogDTO {
    private Long id;
    private String heading;
    private String link;
    private BigDecimal money;
    private LocalDateTime date;
    private List<CategoryDTO> categories;
}
