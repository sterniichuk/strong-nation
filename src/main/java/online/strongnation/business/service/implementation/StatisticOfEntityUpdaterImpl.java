package online.strongnation.business.service.implementation;

import online.strongnation.business.model.statistic.CategoryHolder;
import online.strongnation.business.model.statistic.StatisticEntity;
import online.strongnation.business.model.statistic.StatisticResult;
import online.strongnation.business.model.dto.CategoryDTO;
import online.strongnation.business.service.StatisticOfEntityUpdater;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StatisticOfEntityUpdaterImpl implements StatisticOfEntityUpdater {
    @Override
    public void update(StatisticEntity updated, StatisticResult statistic) {
        Map<CategoryDTO, CategoryDTO> updateMap = CategoryUtils.getCategoryMap(statistic.updatedCategories());
        Map<CategoryDTO, CategoryDTO> excessive = CategoryUtils.getCategoryMap(statistic.excessiveCategories());
        List<? extends CategoryHolder> categories = updated.getCategories();
        var iterator = categories.iterator();
        while (iterator.hasNext()) {
            var next = iterator.next().getCategoryDAO();
            CategoryDTO updatedCategory = updateMap.get(next);
            if (updatedCategory != null) {
                next.setNumber(updatedCategory.getNumber());
                continue;
            }
            CategoryDTO deletedCategory = excessive.get(next);
            if (deletedCategory != null) {
                iterator.remove();
            }
        }
        statistic.newCategories().forEach(updated::addCategory);
    }
}
