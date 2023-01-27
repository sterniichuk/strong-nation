package online.strongnation.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import online.strongnation.config.Floats;
import online.strongnation.model.dto.CategoryDTO;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "category")
public class Category {
    @Id
    @SequenceGenerator(
            name = "category_sequence",
            sequenceName = "category_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "category_sequence"
    )
    @Column(name = "id")
    private Long id;
    @Column(length = 100, nullable = false)
    private String name;
    @Column(nullable = false, scale = Floats.CATEGORY_SCALE)
    private BigDecimal number;
    @Column(length = 10)
    private String units;

    public Category(String name, BigDecimal number, String units) {
        this.name = name;
        this.number = number;
        this.units = units;
    }

    public Category(CategoryDTO categoryDTO) {
        this(categoryDTO.getName(), categoryDTO.getNumber(), categoryDTO.getUnits());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return name.equals(category.name) && Objects.equals(units, category.units);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, units);
    }
}
