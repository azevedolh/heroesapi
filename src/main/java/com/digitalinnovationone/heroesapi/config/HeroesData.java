package com.digitalinnovationone.heroesapi.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;

import static com.digitalinnovationone.heroesapi.constants.HeroesConstant.DYNAMO_ENDPOINT;
import static com.digitalinnovationone.heroesapi.constants.HeroesConstant.DYNAMO_REGION;

public class HeroesData {
    public static void main(String[] args) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(DYNAMO_ENDPOINT, DYNAMO_REGION))
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table heroes = dynamoDB.getTable("heroes");
        Item hero = new Item()
                .withPrimaryKey("id", "1")
                .withString("name", "Superman")
                .withString("universe", "DC Comics")
                .withNumber("movies", 5);

        PutItemOutcome result = heroes.putItem(hero);
    }
}
