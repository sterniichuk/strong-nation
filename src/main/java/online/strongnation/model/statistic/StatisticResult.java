package online.strongnation.model.statistic;

import lombok.Builder;
import online.strongnation.model.dto.CategoryDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Builder(toBuilder = true)
public record StatisticResult
        (Optional<BigDecimal> newMoneyValue,//if empty means that newMoneyValue value is the same
         List<CategoryDTO> newCategories,//if empty means that there is no new categories
         List<CategoryDTO> updatedCategories,//if empty means that there is no updated categories
         List<CategoryDTO> excessiveCategories//if empty means that there is no categories to delete
        ) {
}
