package online.strongnation.business.model.entity;

import jakarta.persistence.*;
import lombok.*;
import online.strongnation.business.model.dto.CategoryDTO;
import online.strongnation.business.model.statistic.CategoryHolder;

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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private CategoryDAO categoryDAO;

    public RegionCategory(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public RegionCategory(CategoryDTO categoryDTO) {
        this(new CategoryDAO(categoryDTO));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegionCategory that = (RegionCategory) o;
        return categoryDAO.equals(that.categoryDAO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryDAO);
    }
}
