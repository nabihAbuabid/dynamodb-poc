package com.dynamodb;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class DynamoDBQUpdateClient<T> extends DynamoDBClient<T> implements DynamoDBUpdateClientIF<T>{
    public DynamoDBQUpdateClient(Class<T> typeParameterClass, String tableName) {
        super(typeParameterClass, tableName);
    }

    /***
     * Add list of items
     * @param items
     */
    public void update(List<T> items) {
        if (items.size() > 0) {
            mapper.batchSave(items);
        }

    }

    /***
     * Upsert implementation - add if not exist, update otherwise
     * @param item
     */
    public void update(T item) {
        mapper.save(item);
    }

    public void getAndpdateAll(DynamoDBQueryClientIF dbQueryClient, Map<String,
                                DynamoDBQueryClient.Operator> operationMap,
                               Map<String, Object> newValMap, T item) {

        Map<String, String> attrTypes = AnnotationInspectionHelper.getFieldsTypes(typeParameterClass);

        List<T> items = dbQueryClient.getAll(operationMap, item);
        Map<Method, Object> methods = new HashMap<>();
        for (String attr : newValMap.keySet()) {
            String setMethod = "set" + attr.substring(0, 1).toUpperCase() + attr.substring(1);
            try {
                String paramType = attrTypes.get(attr);
                Method method = item.getClass().getMethod(setMethod, Class.forName(paramType));
                methods.put(method, newValMap.get(attr));
            } catch (NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        List<T> updatedItems = new ArrayList<>(items);

        updatedItems.forEach(i -> {
            for (Method methodKey : methods.keySet()) {
                try {
                    methodKey.invoke(i, methods.get(methodKey));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });

        update(updatedItems);
    }

}
