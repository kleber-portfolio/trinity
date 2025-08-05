package io.github.mrspock182.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

@Configuration
public class AwsConfig {
    @Bean
    public AwsCredentialsProvider awsCredentialsProvider(
            @Value("${spring.cloud.aws.credentials.access-key}") String accessKey,
            @Value("${spring.cloud.aws.credentials.secret-key}") String secretKey) {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
    }
}