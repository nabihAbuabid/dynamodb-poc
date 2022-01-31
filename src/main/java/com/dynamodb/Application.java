package com.dynamodb;

import com.amazonaws.services.dynamodbv2.xspec.S;
import org.joda.time.DateTime;

import java.util.*;

public class Application {
    public static void main(String[] args) {

        UpdatedItem updatedItem = new UpdatedItem();
        updatedItem.setOrgId("org1");
        updatedItem.setObjectId(UUID.randomUUID().toString());
        updatedItem.setActive(1);
        updatedItem.setObjectType("ServiceAppointment");
        updatedItem.setAction("C");
        updatedItem.setUpdateTimestamp(DateTime.now().toString());
        updatedItem.setExpirationTimestamp((int)(System.currentTimeMillis()/1000) + 36000);


        DynamoDBQueryClient dynamoDBQueryClient = new DynamoDBQueryClient(UpdatedItem.class, "nabih-table");
        DynamoDBQUpdateClient dynamoDBQUpdateClient = new DynamoDBQUpdateClient(UpdatedItem.class, "nabih-table");


        if (args.length == 0) return;

        if ("create".equals(args[0])) {
            CreateTableCompositeKey.createTable("nabih-table");
        } else if ("add".equals(args[0])) {
            dynamoDBQUpdateClient.update(updatedItem);
        } else if ("get".equals(args[0])) {
            UpdatedItem updatedItem1 = new UpdatedItem();
            updatedItem1.setOrgId("org1");
            updatedItem1.setObjectId("0168f195-758a-40c1-9264-ea7314166bbd");

            UpdatedItem retItem = (UpdatedItem) dynamoDBQueryClient.get(updatedItem1);
            System.out.println(retItem);

        } else if ("addall".equals(args[0])) {
            List<UpdatedItem> updatedItems = new ArrayList<>();
            for (int i=0; i<100; i++) {
                UpdatedItem updatedItem1 = new UpdatedItem();
                updatedItem1.setOrgId("org1");
                updatedItem1.setObjectId(UUID.randomUUID().toString());
                updatedItem1.setTerritoryId(UUID.randomUUID().toString());
                updatedItem1.setActive(1);
                updatedItem1.setObjectType("ServiceAppointment");
                updatedItem1.setAction("C");
                updatedItem1.setUpdateTimestamp(DateTime.now().toString());
                updatedItem1.setExpirationTimestamp((int)(System.currentTimeMillis()/1000) + 36000);
                updatedItems.add(updatedItem1);
            }

            for (int i=0; i<100; i++) {
                UpdatedItem updatedItem1 = new UpdatedItem();
                updatedItem1.setOrgId("org1");
                updatedItem1.setObjectId(UUID.randomUUID().toString());
                updatedItem1.setTerritoryId(UUID.randomUUID().toString());
                updatedItem1.setActive(0);
                updatedItem1.setObjectType("ServiceAppointment");
                updatedItem1.setAction("C");
                updatedItem1.setUpdateTimestamp(DateTime.now().toString());
                updatedItem1.setExpirationTimestamp((int)(System.currentTimeMillis()/1000) + 36000);
                updatedItems.add(updatedItem1);
            }
            dynamoDBQUpdateClient.update(updatedItems);

        }else if ("update".equals(args[0])) {
            UpdatedItem updatedItem1 = new UpdatedItem();
            updatedItem1.setOrgId("org1");
            updatedItem1.setObjectId("0168f195-758a-40c1-9264-ea7314166bbd");
            updatedItem1.setActive(1);
            updatedItem1.setObjectType("ServiceAppointment");
            updatedItem1.setAction("C");
            updatedItem1.setUpdateTimestamp(DateTime.now().toString());
            updatedItem1.setExpirationTimestamp((int)(System.currentTimeMillis()/1000) + 36000);

            dynamoDBQUpdateClient.update(updatedItem1);

        } else if ("getall".equals(args[0])) {
            Map<String, DynamoDBQueryClient.Operator> operatorMap = new HashMap<>();
            operatorMap.put("active", DynamoDBQueryClient.Operator.EQUAL);
            UpdatedItem updatedItem2 = new UpdatedItem();
            updatedItem2.setOrgId("org1");
            updatedItem2.setActive(0);

            List<UpdatedItem> items = dynamoDBQueryClient.getAll(operatorMap, updatedItem2);
            System.out.println("retrieved " + items.size() + " items");
//            items.forEach(System.out::println);
        } else if ("getAndUpdate".equals(args[0])) {
            Map<String, DynamoDBQueryClient.Operator> operatorMap = new HashMap<>();
            operatorMap.put("active", DynamoDBQueryClient.Operator.EQUAL);
            UpdatedItem updatedItem2 = new UpdatedItem();
            updatedItem2.setOrgId("org1");
            updatedItem2.setActive(1);

            Map<String, Object> newValMap = new HashMap<>();
            newValMap.put("active", 0);
            newValMap.put("action", "C");
            dynamoDBQUpdateClient.getAndpdateAll(dynamoDBQueryClient,operatorMap,  newValMap, updatedItem2);
        }

    }
}

