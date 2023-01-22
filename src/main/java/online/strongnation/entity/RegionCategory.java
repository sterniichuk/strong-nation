package online.strongnation.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Table(name="region_category",
        uniqueConstraints = {@UniqueConstraint(columnNames={"region_id", "category_id"})})
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
    @EqualsAndHashCode.Exclude
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;
}
