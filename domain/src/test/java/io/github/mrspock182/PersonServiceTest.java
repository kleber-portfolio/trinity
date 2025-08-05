package io.github.mrspock182;

import io.github.mrspock182.entity.Person;
import io.github.mrspock182.entity.enumerable.PillEnum;
import io.github.mrspock182.messaging.PersonProducer;
import io.github.mrspock182.repository.PersonRepository;
import io.github.mrspock182.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PersonServiceTest {
    private PersonService service;
    private PersonProducer producer;
    private PersonRepository repository;

    @BeforeEach
    void setUp() {
        producer = mock(PersonProducer.class);
        repository = mock(PersonRepository.class);
        service = new PersonService(producer, repository);
    }

    @Test
    void shouldFormatPersonCorrectly() {
        Person input = new Person("123", "kleber",  PillEnum.RED, "message", true,
                LocalDate.of(2024, 1, 1));
        Person saved = new Person("123", "KLEBER",  PillEnum.RED, "message", true,
                LocalDate.of(2024, 1, 1));

        when(repository.save(any())).thenReturn(saved);
        when(producer.produce(any())).thenReturn(saved);

        Person result = service.format(input);

        assertNotNull(result);
        assertEquals("KLEBER", result.name());

        verify(repository).save(saved);
        verify(producer).produce(saved);
    }

    @Test
    void shouldThrowIfPersonIsNull() {
        assertThrows(RuntimeException.class, () -> service.format(null));
    }

}
