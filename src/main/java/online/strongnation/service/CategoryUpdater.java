package online.strongnation.service;

import online.strongnation.model.statistic.StatisticEntity;
import online.strongnation.model.statistic.StatisticResult;

public interface CategoryUpdater {
    void update(StatisticEntity updated, StatisticResult statistic);
}
