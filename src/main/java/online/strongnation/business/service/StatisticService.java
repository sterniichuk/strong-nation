package online.strongnation.business.service;

import online.strongnation.business.model.statistic.StatisticModel;
import online.strongnation.business.model.statistic.StatisticResult;

public interface StatisticService {
    <Parent extends StatisticModel<?>, Child extends StatisticModel<Parent>>
    StatisticResult addChildToParent(Parent parent, Child child);

    <Parent extends StatisticModel<?>, Child extends StatisticModel<Parent>>
    StatisticResult updateChild(Parent parent, Child old, Child updated);

    <Parent extends StatisticModel<?>, Child extends StatisticModel<Parent>>
    StatisticResult updateSelf(Child old, Child updated);

    <Parent extends StatisticModel<?>, Child extends StatisticModel<Parent>>
    StatisticResult deleteChild(Parent parent, Child child);
}
