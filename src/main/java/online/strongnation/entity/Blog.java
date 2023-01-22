package online.strongnation.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "blog")
public class Blog {
    @Id
    @SequenceGenerator(
            name = "blog_sequence",
            sequenceName = "blog_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "blog_sequence"
    )
    @Column(name = "id")
    @EqualsAndHashCode.Exclude
    private Long id;
    @Column(nullable = false)
    private String heading;
    @Column(nullable = false)
    private String link;
    private BigDecimal money;

    @Column(nullable = false)
    private LocalDateTime date;
    private LocalDateTime edited;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "blog_photo_id", referencedColumnName = "id")
    private BlogPhoto blogPhoto;

    @OneToMany(targetEntity = BlogCategory.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "blog_id", referencedColumnName = "id", nullable = false)
    private List<BlogCategory> categories;
}
