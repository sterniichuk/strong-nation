package online.strongnation.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Table(name="blog_photo")
public class BlogPhoto {
    @Id
    @SequenceGenerator(
            name = "blog_photo_sequence",
            sequenceName = "blog_photo_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "blog_photo_sequence"
    )
    @Column(name = "id")
    @EqualsAndHashCode.Exclude
    private Long id;
    private String pathToPhoto;
}
