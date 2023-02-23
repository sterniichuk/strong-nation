package online.strongnation.business.model.entity;

import jakarta.persistence.*;
import lombok.*;
import online.strongnation.business.model.statistic.StatisticEntity;
import online.strongnation.business.config.NameProperties;
import online.strongnation.business.model.dto.CategoryDTO;
import online.strongnation.business.model.dto.PostDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "post")
public class Post implements StatisticEntity {
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
    @Column(nullable = false, length = NameProperties.POST_DESCRIPTION_LENGTH)
    private String description;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String link;

    @Column(nullable = false)
    private LocalDateTime date;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_photo_id", referencedColumnName = "id")
    private PostPhoto postPhoto;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean important = false;

    @OneToMany(targetEntity = PostCategory.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private List<PostCategory> categories = new ArrayList<>(0);

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Region region;

    public Post(PostDTO dto) {
        this.description = dto.getDescription();
        this.link = dto.getLink();
        this.date = dto.getDate();
        this.important = dto.getImportant() != null && dto.getImportant();
        setCategoriesDTO(dto.getCategories());
    }

    public void setCategoriesDTO(List<CategoryDTO> categories) {
        if (categories != null && !categories.isEmpty()) {
            this.categories = categories.stream().map(PostCategory::new)
                    .collect(Collectors.toCollection(ArrayList::new));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return description.equals(post.description) && link.equals(post.link) && date.equals(post.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, link, date);
    }

    @Override
    public void addCategory(CategoryDTO category) {
        categories.add(new PostCategory(category));
    }
}
