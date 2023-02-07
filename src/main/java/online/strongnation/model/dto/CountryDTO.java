package online.strongnation.model.dto;

import lombok.*;
import online.strongnation.model.entity.Country;
import online.strongnation.model.entity.CountryCategory;
import online.strongnation.model.statistic.StatisticModel;

import java.util.List;

@Builder(toBuilder = true)
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CountryDTO implements StatisticModel {
    private Long id;
    private String name;
    private List<CategoryDTO> categories;

    public CountryDTO(Country entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        List<CountryCategory> entityCategories = entity.getCategories();
        this.categories = (entityCategories == null) ? List.of() : entityCategories
                .stream().map(CountryCategory::getCategoryDAO)
                .map(CategoryDTO::new).toList();
    }
}
