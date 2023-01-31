package online.strongnation.model.entity;

import jakarta.persistence.*;
import lombok.*;
import online.strongnation.model.dto.CategoryDTO;
import online.strongnation.model.statistic.CategoryHolder;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "region_category",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"region_id", "category_id"})})
public class RegionCategory implements CategoryHolder {

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
    private CategoryEntity categoryEntity;

    public RegionCategory(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    public RegionCategory(CategoryDTO categoryDTO) {
        this(new CategoryEntity(categoryDTO));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegionCategory that = (RegionCategory) o;
        return categoryEntity.equals(that.categoryEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryEntity);
    }
}
