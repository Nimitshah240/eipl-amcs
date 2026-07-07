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


    public static void setFieldValue(Object object, String fieldName, Object value) {
        try {

            Field field = findField(object.getClass(), fieldName);
            field.setAccessible(true);

            Object convertedValue = convertValue(value, field.getType());

            field.set(object, convertedValue);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Field findField(Class<?> clazz, String fieldName)
            throws NoSuchFieldException {

        Class<?> current = clazz;

        while (current != null) {

            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }

        throw new NoSuchFieldException(fieldName);
    }

    private static Object convertValue(Object value, Class<?> type) {

        if (value == null) {
            return null;
        }

        if (type.isAssignableFrom(value.getClass())) {
            return value;
        }

        String text = value.toString();

        if (type == String.class) {
            return text;
        }

        if (type == Integer.class || type == int.class) {
            return Integer.parseInt(text);
        }

        if (type == Long.class || type == long.class) {
            return Long.parseLong(text);
        }

        if (type == Short.class || type == short.class) {
            return Short.parseShort(text);
        }

        if (type == Double.class || type == double.class) {
            return Double.parseDouble(text);
        }

        if (type == Float.class || type == float.class) {
            return Float.parseFloat(text);
        }

        if (type == Boolean.class || type == boolean.class) {
            return Boolean.parseBoolean(text);
        }

        if (type == Byte.class || type == byte.class) {
            return Byte.parseByte(text);
        }

        if (type == Character.class || type == char.class) {
            return text.charAt(0);
        }

        return value;
    }
}