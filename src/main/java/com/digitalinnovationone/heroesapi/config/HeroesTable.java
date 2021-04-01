package com.digitalinnovationone.heroesapi.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

import static com.digitalinnovationone.heroesapi.constants.HeroesConstant.DYNAMO_ENDPOINT;
import static com.digitalinnovationone.heroesapi.constants.HeroesConstant.DYNAMO_REGION;

@Configuration
@EnableDynamoDBRepositories
public class HeroesTable {

    public static void main(String[] args) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(DYNAMO_ENDPOINT, DYNAMO_REGION))
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        String tableName = "heroes";

        Table table = dynamoDB.createTable(tableName,
                Arrays.asList(new KeySchemaElement("id", KeyType.HASH)),
                Arrays.asList(new AttributeDefinition("id", ScalarAttributeType.S)),
                new ProvisionedThroughput(5L, 5L));
    }
}
