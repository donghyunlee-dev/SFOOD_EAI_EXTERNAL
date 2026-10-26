package com.sfood.eai.external.util;

import java.time.LocalTime;

public final class CustomTimeUtils {

    private CustomTimeUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String convertToCron(String type, LocalTime startTime, int delayTime) {

        String second = String.valueOf(startTime.getSecond());
        String minute = String.valueOf(startTime.getMinute());
        String hour = String.valueOf(startTime.getHour());

        // 자동이면
        if ("C".equals(type)) {
            // 시작 시간 설정
            if (delayTime / 60 > 0) {
                hour = hour + "-23" + "/" + delayTime / 60;
                minute = String.valueOf(delayTime % 60);
            } else {
                hour = hour + "-23";
                minute = "*/".concat(String.valueOf(delayTime));
            }
        }

        return String.format("%s %s %s * * ?", second, minute, hour);
    }
}
