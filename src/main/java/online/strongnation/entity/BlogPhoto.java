package online.strongnation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "blog_photo")
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
    private Long id;
    @Column(nullable = false)
    private String pathToPhoto;

    public BlogPhoto(String pathToPhoto) {
        this.pathToPhoto = pathToPhoto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlogPhoto photo = (BlogPhoto) o;
        return pathToPhoto.equals(photo.pathToPhoto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pathToPhoto);
    }
}
