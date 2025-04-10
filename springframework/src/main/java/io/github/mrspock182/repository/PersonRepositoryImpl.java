package io.github.mrspock182.repository;

import io.github.mrspock182.entity.Person;
import io.github.mrspock182.messaging.PersonProducerWithSqs;
import io.github.mrspock182.repository.adapter.PersonRepositoryAdapter;
import io.github.mrspock182.repository.dto.PersonOrmDynamoDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public class PersonRepositoryImpl implements PersonRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonProducerWithSqs.class);

    private final DynamoDbTable<PersonOrmDynamoDB> personTable;

    public PersonRepositoryImpl(DynamoDbTable<PersonOrmDynamoDB> personTable) {
        this.personTable = personTable;
    }

    @Override
    public Person save(final Person person) {
        try {
            final PersonOrmDynamoDB orm = PersonRepositoryAdapter.cast(person);
            personTable.putItem(orm);
            return PersonRepositoryAdapter.cast(orm);
        } catch (Exception ex) {
            LOGGER.error("Error to save person: ", ex);
            throw new RuntimeException("Error to save person", ex);
        }
    }

    @Override
    public Person findById(final String id) {
        try {
            final Optional<PersonOrmDynamoDB> item = Optional.ofNullable(personTable
                    .getItem(r -> r.key(k -> k.partitionValue(id))));
            if (item.isEmpty()) {
                throw new NoSuchElementException("User not found with id: " + id);
            }
            return item.map(PersonRepositoryAdapter::cast).orElse(null);
        } catch (NoSuchElementException ex) {
            LOGGER.error("NoSuchElementException: ", ex);
            throw ex;
        } catch (Exception ex) {
            LOGGER.error("Error to find person: ", ex);
            throw new RuntimeException("Error to find person", ex);
        }
    }
}
