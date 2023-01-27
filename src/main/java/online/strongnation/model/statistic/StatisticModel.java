package online.strongnation.model.statistic;

import online.strongnation.model.dto.CategoryDTO;

import java.math.BigDecimal;
import java.util.List;

public interface StatisticModel {
    BigDecimal getMoney();
    List<CategoryDTO> getCategories();
}
