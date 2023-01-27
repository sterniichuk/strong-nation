package online.strongnation.service.implementation;

import online.strongnation.model.statistic.StatisticModel;
import online.strongnation.model.statistic.StatisticResult;
import online.strongnation.service.StatisticService;
import org.springframework.stereotype.Service;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Override
    public StatisticResult addChildToParent(StatisticModel parent, StatisticModel child) {
        return null;
    }

    @Override
    public StatisticResult updateChild(StatisticModel parent, StatisticModel old, StatisticModel updated) {
        return null;
    }

    @Override
    public StatisticResult deleteChild(StatisticModel parent, StatisticModel child) {
        return null;
    }
}
