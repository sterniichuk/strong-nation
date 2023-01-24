package online.strongnation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDTO {
    private String name;
    private Float number;
    private String units;
}
