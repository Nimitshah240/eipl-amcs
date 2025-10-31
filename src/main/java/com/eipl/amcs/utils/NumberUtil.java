package com.eipl.amcs.utils;

import com.eipl.amcs.MainApp;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtil {
    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();
        if (!Double.isNaN(value)) {
            BigDecimal bd = BigDecimal.valueOf(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        } else {
            return 0;
        }
    }

    public static BigDecimal round(BigDecimal value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value.doubleValue());
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd;
    }

    public static double truncate(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.DOWN);
        return bd.doubleValue();
    }

    public static String twoDecimal(double debit) {
        return MainApp.DECIMAL_FORMAT_2_DIGIT.format(debit);
    }

}
