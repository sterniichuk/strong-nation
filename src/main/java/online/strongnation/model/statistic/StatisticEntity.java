package online.strongnation.model.statistic;

import java.math.BigDecimal;
import java.util.List;

public interface StatisticEntity {
    void setMoney(BigDecimal money);
    List<CategoryHolder> getCategories();
}
