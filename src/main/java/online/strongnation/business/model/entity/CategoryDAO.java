package online.strongnation.business.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import online.strongnation.business.config.Floats;
import online.strongnation.business.config.NameProperties;
import online.strongnation.business.model.Category;
import online.strongnation.business.model.dto.CategoryDTO;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "category")
public class CategoryDAO implements Category {
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
    @Column(length = NameProperties.CATEGORY_NAME_LENGTH, nullable = false)
    private String name;
    @Column(nullable = false, scale = Floats.CATEGORY_SCALE, columnDefinition = "Decimal(38,2) default '0.00'")
    private BigDecimal number;
    @Column(length = NameProperties.CATEGORY_UNITS_LENGTH)
    private String units;

    public CategoryDAO(String name, BigDecimal number, String units) {
        this.name = name;
        this.number = number;
        this.units = units;
    }

    public CategoryDAO(CategoryDTO categoryDTO) {
        this(categoryDTO.getName(), categoryDTO.getNumber(), categoryDTO.getUnits());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryDAO categoryDAO = (CategoryDAO) o;
        return name.equals(categoryDAO.name) && Objects.equals(units, categoryDAO.units);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, units);
    }
}
