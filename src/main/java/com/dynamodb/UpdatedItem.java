package com.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@ToString
@NoArgsConstructor
@Getter
@Setter
@DynamoDBTable(tableName = "nabih-table")
public class UpdatedItem {
    String orgId;
    String objectId;
    String action;
    String territoryId;
    String objectType;
    String updateTimestamp;
    Integer expirationTimestamp;
    Integer active;


    @DynamoDBHashKey(attributeName = "orgId")
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    @DynamoDBRangeKey(attributeName = "objectId")
//    @DynamoDBAttribute(attributeName = "objectId")
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @DynamoDBAttribute(attributeName = "action")
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @DynamoDBAttribute(attributeName = "territoryId")
    public String getTerritoryId() {
        return territoryId;
    }

    public void setTerritoryId(String territoryId) {
        this.territoryId = territoryId;
    }

    @DynamoDBAttribute(attributeName = "objectType")
    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    @DynamoDBAttribute(attributeName = "updateTimestamp")
    public String getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(String updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    @DynamoDBAttribute(attributeName = "active")
    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    @DynamoDBAttribute(attributeName = "expirationTimeSeconds")
    public Integer getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public void setExpirationTimestamp(Integer expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
    }

    @Override
    public String toString() {
        return "UpdatedItem {" +
                "orgId='" + orgId + '\'' +
                ", objectId='" + objectId + '\'' +
                ", active=" + active +
                ", action=" + action +
                '}';
    }
}
