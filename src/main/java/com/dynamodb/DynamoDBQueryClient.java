package com.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.Condition;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamoDBQueryClient<T> extends DynamoDBClient<T> implements DynamoDBQueryClientIF<T>{

    public DynamoDBQueryClient(Class<T> typeParameterClass, String tableName) {
        super(typeParameterClass, tableName);
    }

    /***
     * Get an object by hash and range keys
     * @param item
     * @return
     */
    public T get(T item) {
        return mapper.load(item);
    }

    /***
     * Get all items by certain attribute value
     * @param operationMap
     * @param item - Item holding the query values
     * @return
     */
    public List<T> getAll(Map<String, Operator> operationMap, T item) {
        Map<String, AttributeValue> valMap = new HashMap<>();
        DynamoDBQueryExpression<T> queryExpression = getDynamoDBQueryExpression(item, valMap);

        List<String> attributes = AnnotationInspectionHelper.getAttributesNames(typeParameterClass);
        Map<String, String> fieldsTypes = AnnotationInspectionHelper.getFieldsTypes(typeParameterClass);

        String queryExpressionStr = "";
        for (String attr : attributes) {
            String value = AnnotationInspectionHelper.getFieldValue(item, attr);
            if (StringUtils.isNotEmpty(value)) {
                String fieldType = fieldsTypes.get(attr);
                switch (fieldType) {
                    case "java.lang.String":
                        valMap.put(String.format(":%s", attr), new AttributeValue().withS(value));
                        break;
                    case "java.lang.Integer":
                        valMap.put(String.format(":%s", attr), new AttributeValue().withN(value));
                        break;
                    case "java.lang.Boolean":
                        valMap.put(String.format(":%s", attr),
                                new AttributeValue().withBOOL(Boolean.valueOf(value)));
                        break;
                }
                String tmp = "";
                Operator oper = operationMap.get(attr);
                if (oper != null) {
                    switch (oper) {
                        case EQUAL:
                            tmp = String.format("%s = :%s", attr, attr);
                            break;
                        case LT:
                            tmp = String.format("%s < :%s", attr, attr);
                            break;
                        case LE:
                            tmp = String.format("%s <= :%s", attr, attr);
                            break;
                        case GT:
                            tmp = String.format("%s > :%s", attr, attr);
                            break;
                        case GE:
                            tmp = String.format("%s >= :%s", attr, attr);
                            break;
                        default:
                            break;
                    }
                }
                if (StringUtils.isNotEmpty(tmp)) {
                    if (StringUtils.isNotEmpty(queryExpressionStr)) {
                        queryExpressionStr = String.format("%s and %s", queryExpressionStr, tmp);
                    } else {
                        queryExpressionStr = tmp;
                    }
                }
            }
        }
        queryExpression.withFilterExpression(queryExpressionStr).withExpressionAttributeValues(valMap);
        List<T> allItems = mapper.query(typeParameterClass, queryExpression);

        return allItems;
    }

    private DynamoDBQueryExpression<T> getDynamoDBQueryExpression(T item, Map<String, AttributeValue> valMap) {
        String hashKey = AnnotationInspectionHelper.getPartitionKeyName(typeParameterClass);
        String hashKeyVal = AnnotationInspectionHelper.getFieldValue(item, hashKey);
        String rangeKey = AnnotationInspectionHelper.getRangeKeyName(typeParameterClass);
        String rangeKeyVal = AnnotationInspectionHelper.getFieldValue(item, rangeKey);

        DynamoDBQueryExpression<T> queryExpression = new DynamoDBQueryExpression<T>();


        if (StringUtils.isNotEmpty(hashKeyVal)) {
            valMap.put(String.format(":%s", hashKey), new AttributeValue().withS(hashKeyVal));
            queryExpression.withKeyConditionExpression(String.format("%s = :%s", hashKey, hashKey));
        }

        if (StringUtils.isNotEmpty(rangeKeyVal)) {
            valMap.put(String.format(":%s", rangeKey), new AttributeValue().withS(rangeKeyVal));
            queryExpression.withRangeKeyCondition(rangeKey, new Condition().withAttributeValueList(new AttributeValue(rangeKeyVal)));
        }
        return queryExpression;
    }

    public enum Operator {
        EQUAL("="),
        LE("le"),
        LT("lt"),
        GT("gt"),
        GE("ge");

        private final String op;

        Operator(String oper) {
            this.op = oper;
        }

        public String getOp() {
            return op;
        }
    }
}
