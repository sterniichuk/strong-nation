package online.strongnation.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "post_photo")
public class PostPhoto {
    @Id
    @SequenceGenerator(
            name = "post_photo_sequence",
            sequenceName = "post_photo_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "post_photo_sequence"
    )
    @Column(name = "id")
    private Long id;
    @Column(nullable = false)
    private String pathToPhoto;

    public PostPhoto(String pathToPhoto) {
        this.pathToPhoto = pathToPhoto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostPhoto photo = (PostPhoto) o;
        return pathToPhoto.equals(photo.pathToPhoto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pathToPhoto);
    }
}
