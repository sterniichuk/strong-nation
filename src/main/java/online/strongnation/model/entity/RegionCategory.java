package online.strongnation.model.entity;

import jakarta.persistence.*;
import lombok.*;
import online.strongnation.model.dto.CategoryDTO;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "region_category",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"region_id", "category_id"})})
public class RegionCategory {

    @Id
    @SequenceGenerator(
            name = "region_category_sequence",
            sequenceName = "region_category_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "region_category_sequence"
    )
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;

    public RegionCategory(Category category) {
        this.category = category;
    }

    public RegionCategory(CategoryDTO categoryDTO) {
        this(new Category(categoryDTO));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegionCategory that = (RegionCategory) o;
        return category.equals(that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category);
    }
}
