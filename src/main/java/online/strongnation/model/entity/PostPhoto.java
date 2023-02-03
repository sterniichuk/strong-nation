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
    @Column(columnDefinition = "TEXT", nullable = false)
    private String relativePathToPhoto;

    public PostPhoto(String relativePathToPhoto) {
        this.relativePathToPhoto = relativePathToPhoto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostPhoto photo = (PostPhoto) o;
        return relativePathToPhoto.equals(photo.relativePathToPhoto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(relativePathToPhoto);
    }
}
