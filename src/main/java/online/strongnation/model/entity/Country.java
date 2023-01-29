package online.strongnation.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import online.strongnation.config.Floats;
import online.strongnation.config.NameProperties;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "country")
public class Country {
    @Id
    @SequenceGenerator(
            name = "category_holder_sequence",
            sequenceName = "category_holder_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "category_holder_sequence"
    )
    @Column(name = "id")
    private Long id;
    @Column(unique = true, nullable = false, length = NameProperties.COUNTRY_NAME_LENGTH)
    private String name;
    @ColumnDefault("0")
    @Column(scale = Floats.MONEY_SCALE)
    private BigDecimal money;

    @OneToMany(targetEntity = CountryCategory.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "country_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private List<CountryCategory> categories;

    @OneToMany(
            mappedBy = "country",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    private List<Region> regions = new ArrayList<>(0);

    public void setRegions(List<Region> regions) {
        regions.forEach(r -> r.setCountry(this));
        this.regions = regions;
    }

    public void addRegion(Region region) {
        regions.add(region);
        region.setCountry(this);
    }

    public void removeRegion(Region region) {
        regions.remove(region);
        region.setCountry(null);
    }

    public Country(String name) {
        this.name = name;
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



