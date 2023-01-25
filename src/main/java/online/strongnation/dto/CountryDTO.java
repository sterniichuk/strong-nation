package online.strongnation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

import lombok.NoArgsConstructor;
import online.strongnation.entity.Country;
import online.strongnation.entity.CountryCategory;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CountryDTO {
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
                .stream().map(CountryCategory::getCategory)
                .map(CategoryDTO::new).toList();
    }
}
