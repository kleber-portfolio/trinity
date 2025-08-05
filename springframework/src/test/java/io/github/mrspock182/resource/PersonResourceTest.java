package io.github.mrspock182.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mrspock182.entity.Person;
import io.github.mrspock182.entity.enumerable.PillEnum;
import io.github.mrspock182.repository.PersonRepository;
import io.github.mrspock182.resource.dto.request.PersonRequest;
import io.github.mrspock182.resource.dto.response.PersonResponse;
import io.github.mrspock182.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonResource.class)
class PersonResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PersonService service;

    @MockitoBean
    private PersonRepository repository;

    @Test
    void shouldCreatePerson() throws Exception {
        PersonRequest request = new PersonRequest("kleber");
        Person formatted = new Person("1", "KLEBER", PillEnum.RED, "msg", true,
                LocalDate.parse("2024-01-01"));
        PersonResponse response = new PersonResponse("1", "KLEBER", PillEnum.RED, "msg",
                true, LocalDate.of(2024, 1, 1));

        when(service.format(any())).thenReturn(formatted);

        mockMvc.perform(post("/v1/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value("KLEBER"));
    }

    @Test
    void shouldFindPersonById() throws Exception {
        String id = "1";
        Person person = new Person(id, "KLEBER", PillEnum.RED, "msg", true,
                LocalDate.of(2024, 1, 1));
        when(repository.findById(id)).thenReturn(person);

        mockMvc.perform(get("/v1/person/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("KLEBER"));
    }

}
