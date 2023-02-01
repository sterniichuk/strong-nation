package online.strongnation.config;

import java.math.RoundingMode;

public interface Floats {
    int MONEY_SCALE = 2;
    RoundingMode MONEY_ROUNDING = RoundingMode.HALF_UP;
    int CATEGORY_SCALE = 3;
    RoundingMode CATEGORY_ROUNDING = RoundingMode.HALF_UP;
}
