package online.strongnation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "post_category", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"post_id", "category_id"})
})
public class PostCategory {

    @Id
    @SequenceGenerator(
            name = "post_category_sequence",
            sequenceName = "post_category_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "post_category_sequence"
    )
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    public PostCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostCategory that = (PostCategory) o;
        return category.equals(that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category);
    }
}