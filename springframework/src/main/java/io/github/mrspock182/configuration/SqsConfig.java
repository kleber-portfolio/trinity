package io.github.mrspock182.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration
public class SqsConfig {
    @Bean
    public SqsClient sqsClient(
            @Value("${app.queues.person-endpoint}") final String endpoint,
            @Value("${spring.cloud.aws.region.static}") final String region,
            @Value("${spring.cloud.aws.credentials.access-key}") final String accessKey,
            @Value("${spring.cloud.aws.credentials.secret-key}") final String secretKey) {
        return SqsClient.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }
}