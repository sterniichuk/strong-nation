package online.strongnation.model;

import java.math.BigDecimal;

public interface BigDecimalEquals {
    @SuppressWarnings("all")
    static boolean compare(BigDecimal one, BigDecimal two){
        if(one == two){
            return true;
        }
        if(one == null || two == null){
            return false;
        }
        return one.compareTo(two) == 0;
    }
}
