package com.sfood.eai.external.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Map 관련 커스텀 유틸
 */
@Slf4j
public final class CustomMapUtils {

    private CustomMapUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static MultiValueMap<String, String> convert(Object param) {
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        Field[] fields = param.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.get(param) != null) {
                    paramMap.add(field.getName(), field.get(param).toString());
                }
            } catch (IllegalAccessException e) {
                return null;
            }
        }
        return paramMap;
    }

    public static Map<String, Object> extractFields(Object requestObj) {

        if (ObjectUtils.isEmpty(requestObj)) {
            return new HashMap<>();
        }

        Map<String, Object> fieldMap = new HashMap<>();
        Class<?> clazz = requestObj.getClass();

        while (ObjectUtils.isNotEmpty(clazz)) { // 부모 클래스까지 순회
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true); // private 필드 접근 허용
                try {
                    fieldMap.put(field.getName(), field.get(requestObj));
                } catch (IllegalAccessException e) {
                    log.warn("필드 접근 실패: {}", field.getName(), e);
                }
            }
            clazz = clazz.getSuperclass(); // 부모 클래스로 이동
        }

        return fieldMap;
    }
}
