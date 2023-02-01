package online.strongnation.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import online.strongnation.model.dto.CategoryDTO;
import online.strongnation.model.statistic.CategoryHolder;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private CategoryDAO categoryDAO;

    public CountryCategory(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public CountryCategory(CategoryDTO categoryDTO) {
        this(new CategoryDAO(categoryDTO));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryCategory that = (CountryCategory) o;
        return categoryDAO.equals(that.categoryDAO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryDAO);
    }
}
