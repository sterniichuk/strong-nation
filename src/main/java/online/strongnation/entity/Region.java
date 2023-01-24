package online.strongnation.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "region",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "country_id"})})
public class Region {
    @Id
    @SequenceGenerator(
            name = "region_sequence",
            sequenceName = "region_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "region_sequence"
    )
    @Column(name = "id")
    private Long id;
    @Column(length = 100, nullable = false)
    private String name;
    @ColumnDefault("0")
    private BigDecimal money;

    @OneToMany(targetEntity = RegionCategory.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "region_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private List<RegionCategory> categories;

    @OneToMany(targetEntity = Blog.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "region_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private List<Blog> blogs;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return name.equalsIgnoreCase(region.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
