package online.strongnation.business.model.statistic;

import online.strongnation.business.model.dto.CategoryDTO;

import java.util.List;

public interface StatisticEntity {
    List<? extends CategoryHolder> getCategories();

    void addCategory(CategoryDTO category);
}
