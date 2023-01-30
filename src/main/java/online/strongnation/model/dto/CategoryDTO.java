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

    public CategoryDTO addNumber(CategoryDTO category){
        return new CategoryDTO(
                this.getName(),
                this.getNumber().add(category.getNumber()),
                this.getUnits()
        );
    }

    public CategoryDTO updateNumber(BigDecimal number){
        return new CategoryDTO(
                this.getName(),
                number,
                this.getUnits()
        );
    }
}
