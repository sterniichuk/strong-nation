package online.strongnation.business.model.statistic;

import lombok.Builder;
import online.strongnation.business.model.dto.CategoryDTO;

import java.util.List;

@Builder(toBuilder = true)
public record StatisticResult
        (List<CategoryDTO> newCategories,//if empty means that there are no new categories
         List<CategoryDTO> updatedCategories,//if empty means that there are no updated categories
         List<CategoryDTO> excessiveCategories//if empty means that there are no categories to delete
        ) {
}
