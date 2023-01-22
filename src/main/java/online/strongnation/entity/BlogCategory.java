package online.strongnation.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Table(name = "blog_category", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"blog_id", "category_id"})
})
public class BlogCategory {

    @Id
    @SequenceGenerator(
            name = "blog_category_sequence",
            sequenceName = "blog_category_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "blog_category_sequence"
    )
    @Column(name = "id")
    @EqualsAndHashCode.Exclude
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
}
