package online.strongnation.model.dto;

import lombok.*;
import online.strongnation.model.entity.Region;
import online.strongnation.model.entity.RegionCategory;
import online.strongnation.model.statistic.StatisticModel;

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
