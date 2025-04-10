package io.github.mrspock182.configuration;

import io.github.mrspock182.messaging.PersonProducer;
import io.github.mrspock182.repository.PersonRepository;
import io.github.mrspock182.service.PersonService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonServiceConfig {
    @Bean
    public PersonService personService(
            final PersonProducer producer,
            final PersonRepository repository) {
        return new PersonService(producer, repository);
    }
}