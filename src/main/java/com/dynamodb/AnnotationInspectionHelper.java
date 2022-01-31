package com.dynamodb;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnnotationInspectionHelper<T> {

    public static <T> void getAllAnnotaions(Class<T> clazz){
        Method[] methods = clazz.getMethods();

        for (Method m : methods) {
            if (m.getName().startsWith("get")) {
                Annotation[] annotations = m.getDeclaredAnnotations();
                if (annotations.length > 0) {
                    Arrays.stream(annotations).forEach(anno ->
                            System.out.format("Method %s has annotation %s\n", m.getName(), anno.toString()));
                }
            }
        }
    }

    public static <T> String getPartitionKeyName(Class<T> clazz) {
        Method[] methods = clazz.getDeclaredMethods();

        String partitionKey = Arrays.stream(methods).filter(m -> m.getName().startsWith("get")).map(m -> {
            Annotation[] annotations = m.getDeclaredAnnotations();
            for (Annotation anno : annotations) {
                if (anno.toString().contains("DynamoDBHashKey")) {
                    String methodName = m.getName().substring(3);
                    methodName = methodName.substring(0,1).toLowerCase() + methodName.substring(1);
                    return methodName;
                }
            }
            return "";
        }).findFirst().get();
        return partitionKey;
    }


    public static <T> String getRangeKeyName(Class<T> clazz) {
        Method[] methods = clazz.getDeclaredMethods();

        return Arrays.stream(methods).filter(m -> m.getName().startsWith("get")).map(m -> {
            Annotation[] annotations = m.getDeclaredAnnotations();
            String methodName = "";
            for (Annotation anno : annotations) {
                if (anno.toString().contains("DynamoDBRangeKey")) {
                    methodName = m.getName().substring(3);
                    methodName = methodName.substring(0,1).toLowerCase() + methodName.substring(1);
                    break;
                }
            }
            return methodName;
        }).filter(e -> StringUtils.isNoneEmpty(e)).findFirst().get();
    }


    public static <T> List<String> getAttributesNames(Class<T> clazz) {
        Method[] methods = clazz.getDeclaredMethods();

        return Arrays.stream(methods).filter(m -> m.getName().startsWith("get")).map(m -> {
            Annotation[] annotations = m.getDeclaredAnnotations();
            String methodName = "";
            for (Annotation anno : annotations) {
                if (anno.toString().contains("DynamoDBAttribute")) {
                    methodName = m.getName().substring(3);
                    methodName = methodName.substring(0,1).toLowerCase() + methodName.substring(1);
                    break;
                }
            }
            return methodName;
        }).filter(e -> StringUtils.isNoneEmpty(e)).collect(Collectors.toList());
    }

    public static <T> Map<String, String> getFieldsTypes(Class<T> clazz) {
        Map<String, String> fieldsTypes = new HashMap<>();

        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            String fieldType = declaredField.getType().getTypeName();
            String name=declaredField.getName();
            fieldsTypes.put(name, fieldType);
        }
        return fieldsTypes;
    }

    public static <T> String getFieldValue(T instance, String field) {
        String getMethod = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
        Object value = "";
        try {
            Method method = instance.getClass().getDeclaredMethod(getMethod);
            value = method.invoke(instance);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return value != null ? String.valueOf(value) : null;
    }



}