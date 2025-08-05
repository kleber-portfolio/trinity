package io.github.mrspock182.repository;

import io.github.mrspock182.entity.Person;
import io.github.mrspock182.exception.InternalServerException;
import io.github.mrspock182.exception.NotFoundException;
import io.github.mrspock182.repository.adapter.PersonRepositoryAdapter;
import io.github.mrspock182.repository.dto.PersonOrmDynamoDB;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.Optional;

@Repository
public class PersonRepositoryImpl implements PersonRepository {
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
            throw new InternalServerException("Error to save person", ex);
        }
    }

    @Override
    public Person findById(final String id) {
        try {
            final Optional<PersonOrmDynamoDB> item = Optional.ofNullable(personTable
                    .getItem(r -> r.key(k -> k.partitionValue(id))));
            if (item.isEmpty()) {
                throw new NotFoundException("User not found with id: " + id);
            }
            return item.map(PersonRepositoryAdapter::cast).orElse(null);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalServerException("Error to find person", ex);
        }
    }
}
