package com.eipl.amcs.reportengine.util;

import java.lang.reflect.Field;

public class ReflectionUtil {

    private ReflectionUtil() {
    }

    public static Object getFieldValue(Object object, String fieldName) {

        if (object == null || fieldName == null) {
            return null;
        }

        Class<?> clazz = object.getClass();

        while (clazz != null) {

            try {

                Field field = clazz.getDeclaredField(fieldName);

                field.setAccessible(true);

                return field.get(object);

            } catch (NoSuchFieldException ex) {

                clazz = clazz.getSuperclass();

            } catch (IllegalAccessException ex) {

                throw new RuntimeException(ex);
            }
        }

        throw new RuntimeException(
                "Field '" + fieldName + "' not found in "
                        + object.getClass().getSimpleName());
    }
}