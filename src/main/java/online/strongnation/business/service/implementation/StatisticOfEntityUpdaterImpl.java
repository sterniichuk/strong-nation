package online.strongnation.business.service.implementation;

import online.strongnation.business.model.statistic.StatisticEntity;
import online.strongnation.business.model.statistic.StatisticResult;
import online.strongnation.business.model.dto.CategoryDTO;
import online.strongnation.business.service.StatisticOfEntityUpdater;
import org.springframework.stereotype.Service;

@Service
public class StatisticOfEntityUpdaterImpl implements StatisticOfEntityUpdater {
    @Override
    public void update(StatisticEntity updated, StatisticResult statistic) {
        var updateMap = CategoryUtils.getCategoryMap(statistic.updatedCategories());
        var excessive = CategoryUtils.getCategoryMap(statistic.excessiveCategories());
        var categories = updated.getCategories();
        var iterator = categories.iterator();
        while (iterator.hasNext()) {
            var next = iterator.next().getCategoryDAO();
            CategoryDTO updatedCategory = updateMap.get(next.getName());
            if (updatedCategory != null) {
                next.setNumber(updatedCategory.getNumber());
                continue;
            }
            CategoryDTO deletedCategory = excessive.get(next.getName());
            if (deletedCategory != null) {
                iterator.remove();
            }
        }
        statistic.newCategories().forEach(updated::addCategory);
    }
}
