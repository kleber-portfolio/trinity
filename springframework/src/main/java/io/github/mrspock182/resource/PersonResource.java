package io.github.mrspock182.resource;

import io.github.mrspock182.entity.Person;
import io.github.mrspock182.repository.PersonRepository;
import io.github.mrspock182.resource.adapter.PersonResourceAdapter;
import io.github.mrspock182.resource.dto.request.PersonRequest;
import io.github.mrspock182.resource.dto.response.PersonResponse;
import io.github.mrspock182.service.PersonService;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/v1")
public class PersonResource {
    private final PersonService service;
    private final PersonRepository repository;

    public PersonResource(
            PersonService service,
            PersonRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @ResponseStatus(OK)
    @PostMapping("/person")
    public PersonResponse createPerson(@RequestBody final PersonRequest request) {
        final Person person = PersonResourceAdapter.cast(request);
        return PersonResourceAdapter.cast(service.format(person));
    }

    @ResponseStatus(OK)
    @GetMapping("/person/{id}")
    public PersonResponse findPerson(@PathVariable final String id) {
        final Person person = repository.findById(id);
        return PersonResourceAdapter.cast(person);
    }
}