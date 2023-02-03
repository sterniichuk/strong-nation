package online.strongnation.service;

import online.strongnation.model.statistic.StatisticModel;
import online.strongnation.model.statistic.StatisticResult;

public interface StatisticService {
    StatisticResult addChildToParent(StatisticModel parent, StatisticModel child);

    <T extends StatisticModel> StatisticResult updateChild(StatisticModel parent, T old, T updated);

    <T extends StatisticModel> StatisticResult updateSelf(T old, T updated);

    StatisticResult deleteChild(StatisticModel parent, StatisticModel child);
}
