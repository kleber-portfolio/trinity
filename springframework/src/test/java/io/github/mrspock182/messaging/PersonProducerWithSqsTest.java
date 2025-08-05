package io.github.mrspock182.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mrspock182.entity.Person;
import io.github.mrspock182.entity.enumerable.PillEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PersonProducerWithSqsTest {
    private SqsClient sqsClient;
    private ObjectMapper objectMapper;
    private PersonProducerWithSqs producer;

    @BeforeEach
    void setup() {
        sqsClient = mock(SqsClient.class);
        objectMapper = mock(ObjectMapper.class);
        producer = new PersonProducerWithSqs(
                sqsClient,
                objectMapper,
                "https://sqs.us-east-1.amazonaws.com/***/person-queue");
    }

    @Test
    void shouldSendMessageToSqs() throws Exception {
        Person person = new Person("1", "Kleber", PillEnum.RED, "msg", true,
                LocalDate.of(2024, 1, 1));

        when(objectMapper.writeValueAsString(any()))
                .thenReturn("{\"id\":\"1\",\"name\":\"Kleber\",\"pill\":\"RED\",\"message\":\"msg\"}");

        Person result = producer.produce(person);

        assertEquals(person.id(), result.id());
        verify(sqsClient).sendMessage(any(SendMessageRequest.class));
    }

    @Test
    void shouldThrowExceptionOnJsonProcessingError() throws Exception {
        Person person = new Person("1", "Kleber", PillEnum.RED, "msg", true,
                LocalDate.of(2024, 1, 1));

        when(objectMapper.writeValueAsString(any()))
                .thenThrow(new JsonProcessingException("erro") {
                });

        RuntimeException exception = assertThrows(RuntimeException.class, () -> producer.produce(person));
        assertTrue(exception.getMessage().contains("erro"));
    }

}
