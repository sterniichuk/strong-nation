package online.strongnation.model.statistic;

import online.strongnation.model.dto.CategoryDTO;

import java.math.BigDecimal;
import java.util.List;

public interface StatisticEntity {
    void setMoney(BigDecimal money);

    List<? extends CategoryHolder> getCategories();

    void addCategory(CategoryDTO category);
}
