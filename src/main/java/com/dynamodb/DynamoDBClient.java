package com.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;

public abstract class DynamoDBClient<T> {
    protected static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard().build();
    protected static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
    protected static DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
    protected final Class<T> typeParameterClass;
    protected Table table;

    public DynamoDBClient(Class<T> typeParameterClass, String tableName) {
        this.typeParameterClass = typeParameterClass;
        this.table = dynamoDB.getTable(tableName);
    }
}
