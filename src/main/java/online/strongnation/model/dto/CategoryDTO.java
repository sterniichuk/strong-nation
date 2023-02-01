package online.strongnation.model.dto;

import lombok.*;
import online.strongnation.model.BigDecimalEquals;
import online.strongnation.model.Category;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO implements Category {
    private String name;
    private BigDecimal number;
    private String units;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryDTO that = (CategoryDTO) o;
        return name.equals(that.name) && BigDecimalEquals.compare(number, that.number) && Objects.equals(units, that.units);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, number, units);
    }

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
