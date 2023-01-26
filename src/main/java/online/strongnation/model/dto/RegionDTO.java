package online.strongnation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import online.strongnation.model.entity.RegionCategory;
import online.strongnation.model.entity.Region;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RegionDTO {
    private Long id;
    private String name;
    private BigDecimal money;
    private List<CategoryDTO> categories;

    public RegionDTO(Region entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.money = entity.getMoney();
        List<RegionCategory> entityCategories = entity.getCategories();
        this.categories = (entityCategories == null) ? List.of() : entityCategories
                .stream().map(RegionCategory::getCategory)
                .map(CategoryDTO::new).toList();
    }
}
