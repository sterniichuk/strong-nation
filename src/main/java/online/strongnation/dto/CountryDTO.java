package online.strongnation.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class CountryDTO {
    private Long id;
    private String name;
    private BigDecimal money;
    private List<CategoryDTO> categories;
}
