package com.dynamodb;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;

public class CreateTableCompositeKey {
    public static void createTable(String table_name) {
        CreateTableRequest request = new CreateTableRequest()
                .withAttributeDefinitions(
                        new AttributeDefinition("orgId", ScalarAttributeType.S)
                        ,new AttributeDefinition("objectId", ScalarAttributeType.S)
                )
                .withKeySchema(new KeySchemaElement("orgId", KeyType.HASH)
                        ,new KeySchemaElement("objectId", KeyType.RANGE)
                )
                .withBillingMode(BillingMode.PAY_PER_REQUEST)
                .withTableName(table_name);

        AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDB dynamoDB = new DynamoDB(ddb);
        try {
            Table table = dynamoDB.createTable(request);
            table.waitForActive();

            //table created now enabling TTL
            final UpdateTimeToLiveRequest req = new UpdateTimeToLiveRequest();
            req.setTableName(table_name);

            final TimeToLiveSpecification ttlSpec = new TimeToLiveSpecification();
            ttlSpec.setAttributeName("expirationTimeSeconds");
            ttlSpec.setEnabled(true);
            req.withTimeToLiveSpecification(ttlSpec);

            ddb.updateTimeToLive(req);

        } catch (AmazonServiceException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
