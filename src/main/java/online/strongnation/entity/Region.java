package online.strongnation.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name = "region")
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
    @EqualsAndHashCode.Exclude
    private Long id;
    @Column(length = 100, unique = true, nullable = false)
    private String name;
    @ColumnDefault("0")
    private BigDecimal money;

    @OneToMany(targetEntity = RegionCategory.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "region_id", referencedColumnName = "id", nullable = false)
    private List<RegionCategory> categories;

    @OneToMany(targetEntity = Blog.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "region_id", referencedColumnName = "id", nullable = false)
    private List<Blog> blogs;
}
