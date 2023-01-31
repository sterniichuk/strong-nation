package online.strongnation.service;

import online.strongnation.model.statistic.StatisticEntity;
import online.strongnation.model.statistic.StatisticResult;

public interface StatisticOfEntityUpdater {
    void update(StatisticEntity updated, StatisticResult statistic);
}
