package online.strongnation.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import online.strongnation.config.Floats;
import online.strongnation.config.NameProperties;
import online.strongnation.model.dto.CategoryDTO;
import online.strongnation.model.dto.PostDTO;
import online.strongnation.model.dto.RegionDTO;
import online.strongnation.model.statistic.StatisticEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "region",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "country_id"})})
public class Region implements StatisticEntity {
    @Id
    @SequenceGenerator(
            name = "region_sequence",
            sequenceName = "region_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "region_sequence"
    )
    @Column(name = "id")
    private Long id;
    @Column(nullable = false, length = NameProperties.REGION_NAME_LENGTH)
    private String name;
    @Column(scale = Floats.MONEY_SCALE, columnDefinition = "Decimal(38,2) default '0.00'")
    private BigDecimal money;

    @OneToMany(targetEntity = RegionCategory.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "region_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private List<RegionCategory> categories = new ArrayList<>(0);

    @OneToMany(
            mappedBy = "region",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    private List<Post> posts = new ArrayList<>(0);

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Country country;

    public void setPostsDTO(List<PostDTO> dtoList) {
        this.posts = dtoList.stream().map(x -> {
            Post post = new Post(x);
            post.setRegion(this);
            return post;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    public void setPosts(List<Post> posts) {
        posts.forEach(x -> x.setRegion(this));
        this.posts = posts;
    }

    public Region(RegionDTO dto) {
        this.name = dto.getName();
        this.id = dto.getId();
        this.money = dto.getMoney();
        setCategoriesDTO(dto.getCategories());
    }

    public void setCategoriesDTO(List<CategoryDTO> categories) {
        this.categories = categories.stream().map(CategoryDAO::new).map(RegionCategory::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Region(String name) {
        this.name = name;
    }

    public void addCategory(CategoryDTO category) {
        categories.add(new RegionCategory(category));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return name.equalsIgnoreCase(region.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
