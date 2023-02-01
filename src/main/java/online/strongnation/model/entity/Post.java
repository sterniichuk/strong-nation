package online.strongnation.model.entity;

import jakarta.persistence.*;
import lombok.*;
import online.strongnation.config.Floats;
import online.strongnation.config.NameProperties;
import online.strongnation.model.dto.CategoryDTO;
import online.strongnation.model.dto.PostDTO;
import online.strongnation.model.statistic.StatisticEntity;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
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
    @Column(nullable = false, length = NameProperties.POST_HEADING_LENGTH)
    private String heading;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String link;
    @ColumnDefault("0")
    @Column(scale = Floats.MONEY_SCALE)
    private BigDecimal money;

    @Column(nullable = false)
    private LocalDateTime date;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_photo_id", referencedColumnName = "id")
    private PostPhoto postPhoto;

    @OneToMany(targetEntity = PostCategory.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private List<PostCategory> categories = new ArrayList<>(0);

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Region region;

    public Post(PostDTO dto){
        this.heading = dto.getHeading();
        this.link = dto.getLink();
        this.date = dto.getDate();
        this.money = dto.getMoney();
        setCategoriesDTO(dto.getCategories());
    }

    public void setCategoriesDTO(List<CategoryDTO> categories) {
        if(categories != null && !categories.isEmpty()){
            this.categories = categories.stream().map(PostCategory::new)
                    .collect(Collectors.toCollection(ArrayList::new));
        }
    }

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

    @Override
    public void addCategory(CategoryDTO category) {
        categories.add(new PostCategory(category));
    }
}
