package online.strongnation.business.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import online.strongnation.business.model.dto.CountryDTO;
import online.strongnation.business.model.statistic.StatisticEntity;
import online.strongnation.business.config.NameProperties;
import online.strongnation.business.model.dto.CategoryDTO;
import online.strongnation.business.model.dto.RegionDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "country")
public class Country implements StatisticEntity {
    @Id
    @SequenceGenerator(
            name = "country_sequence",
            sequenceName = "country_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "country_sequence"
    )
    @Column(name = "id")
    private Long id;
    @Column(unique = true, nullable = false, length = NameProperties.COUNTRY_NAME_LENGTH)
    private String name;

    @OneToMany(targetEntity = CountryCategory.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "country_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private List<CountryCategory> categories = new ArrayList<>(0);

    @OneToMany(
            mappedBy = "country",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    private List<Region> regions = new ArrayList<>(0);

    public void addCategory(CategoryDTO category) {
        categories.add(new CountryCategory(category));
    }

    public void setRegions(List<Region> regions) {
        regions.forEach(r -> r.setCountry(this));
        this.regions = regions;
    }

    public void setRegionsDTO(List<RegionDTO> dtoList) {
        this.regions = dtoList.stream().map(x -> {
            Region region = new Region(x);
            region.setCountry(this);
            return region;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    public Country(String name) {
        this.name = name;
    }

    public Country(CountryDTO dto) {
        this.name = dto.getName();
        this.id = dto.getId();
        setCategoriesDTO(dto.getCategories());
    }

    public void setCategoriesDTO(List<CategoryDTO> categories) {
        this.categories = categories.stream().map(CategoryEntity::new).map(CountryCategory::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return name.equals(country.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}



