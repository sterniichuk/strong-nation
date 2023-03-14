package online.strongnation.business.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import online.strongnation.business.model.statistic.CategoryHolder;
import online.strongnation.business.model.dto.CategoryDTO;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "country_category",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"country_id", "category_id"})})
public class CountryCategory implements CategoryHolder {
    @Id
    @SequenceGenerator(
            name = "country_category_sequence",
            sequenceName = "country_category_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "country_category_sequence"
    )
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private CategoryEntity categoryEntity;

    public CountryCategory(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    public CountryCategory(CategoryDTO categoryDTO) {
        this(new CategoryEntity(categoryDTO));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryCategory that = (CountryCategory) o;
        return categoryEntity.equals(that.categoryEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryEntity);
    }
}
