package online.strongnation.business.model.statistic;

import online.strongnation.business.model.dto.CategoryDTO;

import java.util.List;

public interface StatisticModel<Parent extends StatisticModel<?>> {
    List<CategoryDTO> getCategories();
}
