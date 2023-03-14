package online.strongnation.business.model.dto;

import lombok.*;
import online.strongnation.business.model.Category;

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
        return name.equals(that.name) && compare(number, that.number) && Objects.equals(units, that.units);
    }

    @SuppressWarnings("all")
    private static boolean compare(BigDecimal one, BigDecimal two) {
        if (one == two) {
            return true;
        }
        if (one == null || two == null) {
            return false;
        }
        return one.compareTo(two) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, number, units);
    }

    public CategoryDTO(Category categoryEntity) {
        this.name = categoryEntity.getName();
        this.number = categoryEntity.getNumber();
        this.units = categoryEntity.getUnits();
    }

    public CategoryDTO addNumber(CategoryDTO category) {
        return new CategoryDTO(
                this.getName(),
                this.getNumber().add(category.getNumber()),
                this.getUnits()
        );
    }

    public CategoryDTO updateNumber(BigDecimal number) {
        return new CategoryDTO(
                this.getName(),
                number,
                this.getUnits()
        );
    }

    public CategoryDTO getWithUnits(String units) {
        return new CategoryDTO(
                this.getName(),
                this.getNumber(),
                units
        );
    }
}
