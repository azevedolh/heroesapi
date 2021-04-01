package com.digitalinnovationone.heroesapi.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.digitalinnovationone.heroesapi.repository.HeroesRepository;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.digitalinnovationone.heroesapi.constants.HeroesConstant.DYNAMO_REGION;

@Configuration
@EnableDynamoDBRepositories(basePackageClasses = HeroesRepository.class)
public class DynamoConfig {
    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${aws_access_key_id}")
    private String awsAccessKeyId;

    @Value("${aws_secret_access_key}")
    private String awsSecretAccessKey;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
                .standard()
                .withCredentials(amazonAWSCredentials())
                .withEndpointConfiguration(amazonAWSEndpoint())
                .build();

        return amazonDynamoDB;
    }

    @Bean
    public AWSStaticCredentialsProvider amazonAWSCredentials() {
        return new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey
                )
        );
    }

    @Bean
    public AwsClientBuilder.EndpointConfiguration amazonAWSEndpoint() {
        return new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, DYNAMO_REGION);
    }
}

