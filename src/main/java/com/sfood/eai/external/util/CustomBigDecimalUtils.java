package com.sfood.eai.external.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Objects;

public final class CustomBigDecimalUtils {

    private CustomBigDecimalUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static BigDecimal subtract(BigDecimal value1, BigDecimal value2) {

        if(value1 == null || value2 == null) {
            return BigDecimal.ZERO;
        }

        return value1.subtract(value2);
    }

    public static BigDecimal add(BigDecimal... prices) {
        if (prices == null || prices.length == 0) {
            return BigDecimal.ZERO;
        }

        return Arrays.stream(prices)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal multiply(BigDecimal value1, BigDecimal value2) {

        if(value1 == null || value2 == null) {
            return BigDecimal.ZERO;
        }

        return value1.multiply(value2).setScale(0, RoundingMode.DOWN);
    }

    public static BigDecimal nullIfZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    /**
     * null-safe BigDecimal 비교 (compareTo 기준)
     *
     * @param a 첫 번째 BigDecimal
     * @param b 두 번째 BigDecimal
     * @return a와 b가 같으면 true (둘 다 null도 true), 다르면 false
     */
    public static boolean equals(BigDecimal a, BigDecimal b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.compareTo(b) == 0;
    }

    /**
     * null-safe BigDecimal 비교 (다르면 true)
     *
     * @param a 첫 번째 BigDecimal
     * @param b 두 번째 BigDecimal
     * @return a와 b가 다르면 true, 같으면 false
     */
    public static boolean notEquals(BigDecimal a, BigDecimal b) {
        return !equals(a, b);
    }
}
