package online.strongnation.model.statistic;

import online.strongnation.model.dto.CategoryDTO;

import java.util.List;

public interface StatisticEntity {
    List<? extends CategoryHolder> getCategories();

    void addCategory(CategoryDTO category);
}
