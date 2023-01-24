package online.strongnation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "post")
public class Post {
    @Id
    @SequenceGenerator(
            name = "post_sequence",
            sequenceName = "post_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "post_sequence"
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
    @JoinColumn(name = "post_photo_id", referencedColumnName = "id")
    private PostPhoto postPhoto;

    @OneToMany(targetEntity = PostCategory.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private List<PostCategory> categories;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return heading.equals(post.heading) && link.equals(post.link) && date.equals(post.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(heading, link, date);
    }
}
