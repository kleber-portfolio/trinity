package io.github.mrspock182.repository;

import io.github.mrspock182.entity.Person;
import io.github.mrspock182.entity.enumerable.PillEnum;
import io.github.mrspock182.exception.NotFoundException;
import io.github.mrspock182.repository.dto.PersonOrmDynamoDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.time.LocalDate;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonRepositoryImplTest {

    private DynamoDbTable<PersonOrmDynamoDB> table;
    private PersonRepositoryImpl repository;

    @BeforeEach
    void setup() {
        table = mock(DynamoDbTable.class);
        repository = new PersonRepositoryImpl(table);
    }

    @Test
    void shouldSavePerson() {
        Person person = new Person("1", "Kleber", PillEnum.RED, "msg", true,
                LocalDate.of(2024, 1, 1));
        PersonOrmDynamoDB orm = new PersonOrmDynamoDB();
        orm.setId("1");
        orm.setName("KLEBER");
        orm.setPill(PillEnum.RED);
        orm.setMessage("msg");
        orm.setChosen(true);
        orm.setCreateIn(LocalDate.of(2024, 1, 1));

        doNothing().when(table).putItem(any(PersonOrmDynamoDB.class));

        Person result = repository.save(person);

        assertNotNull(result);
        verify(table).putItem(any(PersonOrmDynamoDB.class));
    }

    @Test
    void shouldFindPersonById() {
        PersonOrmDynamoDB orm = new PersonOrmDynamoDB();
        orm.setId("1");
        orm.setName("KLEBER");
        orm.setPill(PillEnum.RED);
        orm.setMessage("msg");
        orm.setChosen(true);
        orm.setCreateIn(LocalDate.of(2024, 1, 1));

        when(table.getItem(any(Consumer.class))).thenReturn(orm);

        Person result = repository.findById("1");

        assertNotNull(result);
        assertEquals("KLEBER", result.name());
    }

    @Test
    void shouldThrowNoSuchElementWhenNotFound() {
        when(table.getItem(any(Key.class))).thenReturn(null);
        assertThrows(NotFoundException.class, () -> repository.findById("not-found"));
    }

    @Test
    void shouldThrowRuntimeExceptionOnSaveError() {
        Person person = new Person("1", "Kleber", PillEnum.RED, "msg", true,
                LocalDate.of(2024, 1, 1));

        doThrow(new RuntimeException("erro")).when(table).putItem(any(PersonOrmDynamoDB.class));

        assertThrows(RuntimeException.class, () -> repository.save(person));
    }

}
