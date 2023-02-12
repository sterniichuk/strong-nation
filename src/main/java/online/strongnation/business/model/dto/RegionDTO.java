package online.strongnation.business.model.dto;

import lombok.*;
import online.strongnation.business.model.entity.Region;
import online.strongnation.business.model.statistic.StatisticModel;
import online.strongnation.business.model.entity.RegionCategory;

import java.util.List;

@Builder(toBuilder = true)
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class RegionDTO implements StatisticModel {
    private Long id;
    private String name;
    private List<CategoryDTO> categories;

    public RegionDTO(Region entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        List<RegionCategory> entityCategories = entity.getCategories();
        this.categories =
                (entityCategories == null || entityCategories.isEmpty()) ? List.of() : entityCategories
                        .stream().map(RegionCategory::getCategoryDAO)
                        .map(CategoryDTO::new).toList();
    }
}
