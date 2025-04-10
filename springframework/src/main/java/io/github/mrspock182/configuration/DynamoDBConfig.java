package io.github.mrspock182.configuration;

import io.github.mrspock182.entity.enumerable.PillEnum;
import io.github.mrspock182.repository.dto.PersonOrmDynamoDB;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticTableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.time.LocalDate;

@Configuration
public class DynamoDBConfig {
    @Bean
    public DynamoDbClient dynamoDbClient(
            @Value("${spring.cloud.aws.region.static}") final String region) {
        return DynamoDbClient.builder()
                .region(Region.of(region))
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient enhancedClient(DynamoDbClient client) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .build();
    }

    @Bean
    public DynamoDbTable<PersonOrmDynamoDB> dynamoDbTablePerson(
            final DynamoDbEnhancedClient enhancedClient,
            @Value("${spring.cloud.aws.dynamodb.person-table}") final String tableName) {

        TableSchema<PersonOrmDynamoDB> tableSchema = StaticTableSchema.builder(PersonOrmDynamoDB.class)
                .newItemSupplier(PersonOrmDynamoDB::new)
                .addAttribute(String.class, a -> a.name("id")
                        .getter(PersonOrmDynamoDB::getId)
                        .setter(PersonOrmDynamoDB::setId)
                        .tags(StaticAttributeTags.primaryPartitionKey()))
                .addAttribute(String.class, a -> a.name("name")
                        .getter(PersonOrmDynamoDB::getName)
                        .setter(PersonOrmDynamoDB::setName))
                .addAttribute(String.class, a -> a.name("message")
                        .getter(PersonOrmDynamoDB::getMessage)
                        .setter(PersonOrmDynamoDB::setMessage))
                .addAttribute(Boolean.class, a -> a.name("chosen")
                        .getter(PersonOrmDynamoDB::getChosen)
                        .setter(PersonOrmDynamoDB::setChosen))
                .addAttribute(String.class, a -> a.name("pill")
                        .getter(p -> p.getPill() != null ? p.getPill().name() : null)
                        .setter((p, value) -> p.setPill(value != null ? PillEnum.valueOf(value) : null)))
                .addAttribute(String.class, a -> a.name("createIn")
                        .getter(p -> p.getCreateIn() != null ? p.getCreateIn().toString() : null)
                        .setter((p, value) -> p.setCreateIn(value != null ? LocalDate.parse(value) : null)))
                .build();

        return enhancedClient.table(tableName, tableSchema);
    }
}