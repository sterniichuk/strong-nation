package online.strongnation.business.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "slider_photo")
public class SliderPhoto {

    public static final String sequence = "slider_photo_sequence";
    @Id
    @SequenceGenerator(
            name = sequence,
            sequenceName = sequence,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = sequence
    )
    @Column(name = "id")
    private Long id;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String relativePathToPhoto;

    public SliderPhoto(String relativePathToPhoto) {
        this.relativePathToPhoto = relativePathToPhoto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SliderPhoto photo = (SliderPhoto) o;
        return relativePathToPhoto.equals(photo.relativePathToPhoto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(relativePathToPhoto);
    }
}
