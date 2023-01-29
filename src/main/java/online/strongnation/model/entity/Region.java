package online.strongnation.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import online.strongnation.config.Floats;
import online.strongnation.config.NameProperties;
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
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "category_holder_sequence"
    )
    @Column(name = "id")
    private Long id;
    @Column(nullable = false, length = NameProperties.REGION_NAME_LENGTH)
    private String name;
    @ColumnDefault("0")
    @Column(scale = Floats.MONEY_SCALE)
    private BigDecimal money;

    @OneToMany(targetEntity = RegionCategory.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "region_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private List<RegionCategory> categories;

    @OneToMany(targetEntity = Post.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "region_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private List<Post> posts;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Country country;

    public Region(String name) {
        this.name = name;
    }

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
