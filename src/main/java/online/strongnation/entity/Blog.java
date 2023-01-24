package online.strongnation.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
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
    private Long id;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String heading;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String link;
    private BigDecimal money;

    @Column(nullable = false)
    private LocalDateTime date;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "blog_photo_id", referencedColumnName = "id")
    private BlogPhoto blogPhoto;

    @OneToMany(targetEntity = BlogCategory.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "blog_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private List<BlogCategory> categories;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blog blog = (Blog) o;
        return heading.equals(blog.heading) && link.equals(blog.link) && date.equals(blog.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(heading, link, date);
    }
}
