package com.dynamodb;

import java.util.List;
import java.util.Map;

public interface DynamoDBUpdateClientIF<T> {
    void update(List<T> items);

    void update(T item);

    void getAndpdateAll(DynamoDBQueryClientIF dbQueryClient, Map<String,
            DynamoDBQueryClient.Operator> operationMap,
                        Map<String, Object> newValMap, T item);
}
