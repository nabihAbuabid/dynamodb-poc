package com.dynamodb;

import java.util.List;
import java.util.Map;

public interface DynamoDBQueryClientIF<T> {
    T get(T item);

    List<T> getAll(Map<String, DynamoDBQueryClient.Operator> operationMap, T item);

}
