package io.github.mrspock182.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mrspock182.entity.Person;
import io.github.mrspock182.exception.InternalServerException;
import io.github.mrspock182.messaging.adapter.PersonProducerAdapter;
import io.github.mrspock182.messaging.dto.PersonProducerDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
public class PersonProducerWithSqs implements PersonProducer {
    private final String topic;
    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    public PersonProducerWithSqs(
            SqsClient sqsClient,
            ObjectMapper objectMapper,
            @Value("${app.queues.person-endpoint}") String topic) {
        this.topic = topic;
        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public Person produce(final Person person) {
        try {
            final PersonProducerDto producer = PersonProducerAdapter.cast(person);
            final String json = objectMapper.writeValueAsString(producer);
            final SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(topic)
                    .messageBody(json)
                    .build();
            sqsClient.sendMessage(request);
            return PersonProducerAdapter.cast(producer);
        } catch (RuntimeException | JsonProcessingException ex) {
            throw new InternalServerException(ex);
        }
    }
}