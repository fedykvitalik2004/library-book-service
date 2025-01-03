package org.vitalii.fedyk.librarybookservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.vitalii.fedyk.librarybookservice.dto.CreateAuthorDto;
import org.vitalii.fedyk.librarybookservice.dto.FullNameDto;
import org.vitalii.fedyk.librarybookservice.model.Author;
import org.vitalii.fedyk.librarybookservice.repository.AuthorRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
@ActiveProfiles("test")
@Transactional
class AuthorControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql("/data.sql")
    void testDeleteAuthor_OperationNotPermitted() throws Exception {
        final long userId = 1L;
        mockMvc.perform(delete("/users/{authorId}", userId))
                .andExpect(status().isConflict());
        assertTrue(authorRepository.findById(userId).isPresent());
    }

    @Test
    @Sql("/data.sql")
    void testDeleteAuthor_Success() throws Exception {
        final long authorId = 2L;
        mockMvc.perform(delete("/authors/{authorId}", authorId))
                .andExpect(status().isNoContent());
        assertTrue(authorRepository.findById(authorId).isEmpty());
    }

    @Test
    @Sql("/data.sql")
    void testDeleteAuthor_Conflict() throws Exception {
        final long authorId = 1L;
        mockMvc.perform(delete("/authors/{authorId}", authorId))
                .andExpect(status().isConflict());
    }

    @Test
    @Sql("/data.sql")
    void testDeleteAuthor_NoFound() throws Exception {
        final long authorId = 1000L;
        mockMvc.perform(delete("/authors/{authorId}", authorId))
                .andExpect(status().isNotFound());
        assertTrue(authorRepository.findById(authorId).isEmpty());
    }

    @Test
    void testGetAuthorById_NotFound() throws Exception {
        mockMvc.perform(get("/authors/{authorId}", 100L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql("/data.sql")
    void testGetAuthorById_Success() throws Exception {
        final long authorId = 1L;
        final Author author = authorRepository.findById(authorId).orElseThrow();
        mockMvc.perform(get("/authors/{authorId}", authorId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(author.getId()))
                .andExpect(jsonPath("$.description").value(author.getDescription()))
                .andExpect(jsonPath("$.fullName.firstName").value(author.getFullName().getFirstName()))
                .andExpect(jsonPath("$.fullName.lastName").value(author.getFullName().getLastName()));
    }

    @Test
    @Sql("/data.sql")
    void testGetAllAuthors_Success() throws Exception {
        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql("/data.sql")
    void testCreateAuthor_Success() throws Exception {
        final long elementsBefore = authorRepository.findAll().size();
        final CreateAuthorDto createAuthorDto = new CreateAuthorDto();
        createAuthorDto.setFullName(new FullNameDto().firstName("Jannet").lastName("Doe"));
        createAuthorDto.setDescription("Description for Jannet");
        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAuthorDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        final long elementsAfter = authorRepository.findAll().size();
        assertEquals(1, elementsAfter - elementsBefore);
    }

    @Test
    @Sql("/data.sql")
    void testCreateAuthor_BadRequest() throws Exception {
        final CreateAuthorDto createAuthorDto = new CreateAuthorDto();
        createAuthorDto.setFullName(null);

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAuthorDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql("/data.sql")
    void testUpdateAuthor_NotFound() throws Exception {
        final CreateAuthorDto createAuthorDto = new CreateAuthorDto();
        createAuthorDto.setFullName(new FullNameDto().firstName("ChangedFirstName").lastName("ChangedLastName"));
        createAuthorDto.setDescription("Changed");

        mockMvc.perform(put("/authors/{authorId}", 1000L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAuthorDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql("/data.sql")
    void testUpdateAuthor_BadRequest() throws Exception {
        final CreateAuthorDto createAuthorDto = new CreateAuthorDto();
        createAuthorDto.setDescription("Changed");

        mockMvc.perform(put("/authors/{authorId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAuthorDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql("/data.sql")
    void testUpdateAuthor_Success() throws Exception {
        final long authorId = 1L;
        final CreateAuthorDto createAuthorDto = new CreateAuthorDto();
        createAuthorDto.setFullName(new FullNameDto().firstName("ChangedFirstName").lastName("ChangedLastName"));
        createAuthorDto.setDescription("Changed");

        mockMvc.perform(put("/authors/{authorId}", authorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAuthorDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        final Author author = authorRepository.findById(authorId).orElseThrow();
        assertEquals(createAuthorDto.getFullName().getFirstName(), author.getFullName().getFirstName());
        assertEquals(createAuthorDto.getFullName().getLastName(), author.getFullName().getLastName());
        assertEquals(createAuthorDto.getDescription(), author.getDescription());
    }
}
