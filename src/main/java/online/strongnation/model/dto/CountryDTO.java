package online.strongnation.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

import online.strongnation.model.entity.Country;
import online.strongnation.model.entity.CountryCategory;
import online.strongnation.model.statistic.StatisticModel;

@Builder(toBuilder = true)
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CountryDTO implements StatisticModel {
    private Long id;
    private String name;
    private BigDecimal money;
    private List<CategoryDTO> categories;

    public CountryDTO(Country entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.money = entity.getMoney();
        List<CountryCategory> entityCategories = entity.getCategories();
        this.categories = (entityCategories == null) ? List.of() : entityCategories
                .stream().map(CountryCategory::getCategoryEntity)
                .map(CategoryDTO::new).toList();
    }
}
