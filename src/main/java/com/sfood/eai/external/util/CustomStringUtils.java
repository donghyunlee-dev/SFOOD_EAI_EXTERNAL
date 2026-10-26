package com.sfood.eai.external.util;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public final class CustomStringUtils {

    private CustomStringUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String substringByBytes(String data, int beginBytes, int endBytes) {

        if (!StringUtils.hasText(data)) {
            return "";
        }

        if (endBytes < 1) {
            return "";
        }

        if (beginBytes < 0) {
            beginBytes = 0;
        }

        int beginIndex = -1;
        int endIndex = 0;
        int currBytes = 0;

        String word;

        for (int i = 0; i < data.length(); i++) {
            word = data.substring(i, i + 1);

            currBytes += word.getBytes().length;

            if (beginIndex == -1 && currBytes >= beginBytes) {
                beginIndex = i;
            }

            if (currBytes > endBytes) {
                break;
            } else {
                endIndex = i + 1;
            }
        }

        return data.substring(beginIndex, endIndex);
    }

    public static int[] extractNumbers(String value) {
        List<Integer> numbers = new ArrayList<>();
        StringBuilder numBuffer = new StringBuilder();

        for (char c : value.toCharArray()) {
            if (Character.isDigit(c)) {
                numBuffer.append(c);
            } else if (numBuffer.length() > 0) {
                numbers.add(Integer.parseInt(numBuffer.toString()));
                numBuffer.setLength(0);
            }
        }

        if (numBuffer.length() > 0) {
            numbers.add(Integer.parseInt(numBuffer.toString()));
        }

        return numbers.stream().mapToInt(i -> i).toArray();
    }

    public static boolean equalsIgnoreNullOrEmpty(String a, String b) {
        return (a == null || a.isEmpty() ? "" : a)
                .equals(b == null || b.isEmpty() ? "" : b);
    }

    public static boolean notEqualsIgnoreNullOrEmpty(String a, String b) {
        return !equalsIgnoreNullOrEmpty(a, b);
    }
}
