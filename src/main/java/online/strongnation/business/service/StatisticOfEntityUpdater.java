package online.strongnation.business.service;

import online.strongnation.business.model.statistic.StatisticEntity;
import online.strongnation.business.model.statistic.StatisticResult;

public interface StatisticOfEntityUpdater {
    void update(StatisticEntity updated, StatisticResult statistic);
}
