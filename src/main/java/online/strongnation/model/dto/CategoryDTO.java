package online.strongnation.model.dto;

import lombok.*;
import online.strongnation.model.Category;

import java.math.BigDecimal;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO implements Category {
    private String name;
    private BigDecimal number;
    private String units;

    public CategoryDTO(Category categoryEntity){
        this.name = categoryEntity.getName();
        this.number = categoryEntity.getNumber();
        this.units = categoryEntity.getUnits();
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
