package online.strongnation.service.implementation;

import online.strongnation.model.dto.CategoryDTO;
import online.strongnation.model.statistic.StatisticEntity;
import online.strongnation.model.statistic.StatisticResult;
import online.strongnation.service.StatisticOfEntityUpdater;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static online.strongnation.service.implementation.CategoryUtils.getCategoryMap;

@Service
public class StatisticOfEntityUpdaterImpl implements StatisticOfEntityUpdater {
    @Override
    public void update(StatisticEntity updated, StatisticResult statistic) {
        statistic.getNewMoneyValueOpt().ifPresent(updated::setMoney);
        var updateMap = getCategoryMap(statistic.updatedCategories());
        var excessive = getCategoryMap(statistic.excessiveCategories());
        var categories = updated.getCategories();
        var iterator = categories.iterator();
        while (iterator.hasNext()){
            var next = iterator.next().getCategoryEntity();
            CategoryDTO updatedCategory = updateMap.get(next.getName());
            if(updatedCategory != null && Objects.equals(updatedCategory.getUnits(), next.getUnits())){
                next.setNumber(updatedCategory.getNumber());
                continue;
            }
            CategoryDTO deletedCategory = excessive.get(next.getName());
            if(deletedCategory != null && Objects.equals(deletedCategory.getUnits(), next.getUnits())){
                iterator.remove();
            }
        }
        statistic.newCategories().forEach(updated::addCategory);
    }
}
