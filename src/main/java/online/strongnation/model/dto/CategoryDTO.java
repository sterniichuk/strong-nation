package online.strongnation.model.dto;

import lombok.*;
import online.strongnation.model.entity.Category;

import java.math.BigDecimal;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private String name;
    private BigDecimal number;
    private String units;

    public CategoryDTO(Category category){
        this.name = category.getName();
        this.number = category.getNumber();
        this.units = category.getUnits();
    }
}
