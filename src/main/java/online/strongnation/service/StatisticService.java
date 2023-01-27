package online.strongnation.service;

import online.strongnation.model.statistic.StatisticModel;
import online.strongnation.model.statistic.StatisticResult;

public interface StatisticService {
    StatisticResult addChildToParent(StatisticModel parent, StatisticModel child);
    StatisticResult updateChild(StatisticModel parent, StatisticModel old, StatisticModel updated);
    StatisticResult deleteChild(StatisticModel parent, StatisticModel child);
}
