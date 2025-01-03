package org.vitalii.fedyk.librarybookservice.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.vitalii.fedyk.librarybookservice.dto.CreateAuthorDto;
import org.vitalii.fedyk.librarybookservice.dto.FullNameDto;
import org.vitalii.fedyk.librarybookservice.dto.ReadAuthorDto;
import org.vitalii.fedyk.librarybookservice.dto.ReadAuthorsDto;
import org.vitalii.fedyk.librarybookservice.exception.NotFoundException;
import org.vitalii.fedyk.librarybookservice.exception.OperationNotPermittedException;
import org.vitalii.fedyk.librarybookservice.model.Author;
import org.vitalii.fedyk.librarybookservice.model.Book;
import org.vitalii.fedyk.librarybookservice.model.BookGenre;
import org.vitalii.fedyk.librarybookservice.model.FullName;
import org.vitalii.fedyk.librarybookservice.repository.AuthorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class AuthorServiceImplTest {
    @Autowired
    private AuthorService authorService;
    @MockitoBean
    private AuthorRepository authorRepository;
    @MockitoBean
    private BookService bookService;

    @Test
    void testReadAuthor_AuthorNotFound() {
        final long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authorService.readAuthor(authorId));

        verify(authorRepository).findById(authorId);
    }

    @Test
    void testReadAuthor_SuccessfulRetrieval() {
        final long authorId = 1L;
        final Author author = new Author(authorId, new FullName("Jane", "Doe"), "Description", new ArrayList<>());
        final Book book = new Book(2L, "Title", "Description", BookGenre.BIOGRAPHY, 120, author);
        author.getBooks().add(book);
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        final ReadAuthorDto result = authorService.readAuthor(authorId);

        assertNotNull(result);
        assertEquals(author.getId(), result.getId());
        assertEquals(author.getFullName().getFirstName(), result.getFullName().getFirstName());
        assertEquals(author.getFullName().getLastName(), result.getFullName().getLastName());

        verify(authorRepository).findById(authorId);
    }

    @Test
    void testDeleteAuthor_AuthorNotFound() {
        final long authorId = 1L;
        when(authorRepository.existsById(authorId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> authorService.deleteAuthor(authorId));

        verify(authorRepository).existsById(authorId);
        verify(authorRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteAuthor_AuthorHasBooks() {
        final long authorId = 1L;
        when(authorRepository.existsById(authorId)).thenReturn(true);
        when(bookService.authorHasBooks(authorId)).thenReturn(true);

        assertThrows(OperationNotPermittedException.class, () -> authorService.deleteAuthor(authorId));

        verify(authorRepository).existsById(authorId);
        verify(bookService).authorHasBooks(authorId);
        verify(authorRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteAuthor_SuccessfulDeletion() {
        final long authorId = 1L;
        when(authorRepository.existsById(authorId)).thenReturn(true);
        when(bookService.authorHasBooks(authorId)).thenReturn(false);

        authorService.deleteAuthor(authorId);

        verify(authorRepository).deleteById(authorId);
        verify(authorRepository).existsById(authorId);
        verify(bookService).authorHasBooks(authorId);
    }

    @Test
    void testFindAll_AuthorsFound() {
        final long authorId = 1L;
        final Author author = new Author(authorId, new FullName("Jane", "Doe"), "Description", new ArrayList<>());
        final Book book = new Book(2L, "Title", "Description", BookGenre.BIOGRAPHY, 120, author);
        author.getBooks().add(book);
        final Page<Author> authorsPage = new PageImpl<>(List.of(author));
        final Pageable pageable = PageRequest.of(0, 1);
        when(authorRepository.findAll(pageable)).thenReturn(authorsPage);

        final ReadAuthorsDto result = authorService.findAll(pageable);

        final ReadAuthorDto element = result.getAuthors().get(0);
        assertNotNull(result);
        assertEquals(author.getId(), result.getAuthors().size());
        assertEquals(author.getFullName().getFirstName(), element.getFullName().getFirstName());
        assertEquals(author.getFullName().getLastName(), element.getFullName().getLastName());
        assertEquals(author.getDescription(), element.getDescription());

        verify(authorRepository).findAll(pageable);
    }

    @Test
    void testFindAll_NoAuthorsFound() {
        final Page<Author> authorsPage = new PageImpl<>(List.of());
        final Pageable pageable = PageRequest.of(0, 1);
        when(authorRepository.findAll(pageable)).thenReturn(authorsPage);

        final ReadAuthorsDto result = authorService.findAll(pageable);

        assertNotNull(result);
        assertTrue(result.getAuthors().isEmpty());

        verify(authorRepository).findAll(pageable);
    }

    @Test
    void testCreateAuthor_SuccessfulCreation() {
        final long authorId = 1L;
        final Author author = new Author(authorId, new FullName("Jane", "Doe"), "Description", new ArrayList<>());
        final Book book = new Book(2L, "Title", "Description", BookGenre.BIOGRAPHY, 120, author);
        author.getBooks().add(book);
        final CreateAuthorDto createAuthorDto = new CreateAuthorDto(new FullNameDto().firstName("Jane").lastName("Doe"));
        createAuthorDto.setDescription("Description");

        when(authorRepository.save(any(Author.class))).thenReturn(author);

        final ReadAuthorDto result = authorService.createAuthor(createAuthorDto);

        assertNotNull(result);
        assertEquals(author.getId(), result.getId());
        assertEquals(author.getFullName().getFirstName(), result.getFullName().getFirstName());
        assertEquals(author.getFullName().getLastName(), result.getFullName().getLastName());
        assertEquals(author.getDescription(), result.getDescription());

        verify(authorRepository).save(any(Author.class));
    }

    @Test
    void testUpdateAuthor_AuthorNotFound() {
        final long authorId = 1L;
        final CreateAuthorDto createAuthorDto = new CreateAuthorDto(new FullNameDto().firstName("Jane").lastName("Doe"));
        createAuthorDto.setDescription("Description");

        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authorService.updateAuthor(authorId, createAuthorDto));

        verify(authorRepository).findById(authorId);
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void testUpdateAuthor_SuccessfulUpdate() {
        final long authorId = 1L;
        final Author author = new Author(authorId, new FullName("Jane", "Doe"), "Description", new ArrayList<>());
        final Book book = new Book(2L, "Title", "Description", BookGenre.BIOGRAPHY, 120, author);
        author.getBooks().add(book);
        final CreateAuthorDto createAuthorDto = new CreateAuthorDto();
        createAuthorDto.setFullName(new FullNameDto().firstName("Jane").lastName("Doe"));
        createAuthorDto.setDescription("Changed");

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        final ReadAuthorDto result = authorService.updateAuthor(authorId, createAuthorDto);

        assertNotNull(result);
        assertEquals(createAuthorDto.getFullName(), result.getFullName());
        assertEquals(createAuthorDto.getDescription(), result.getDescription());

        verify(authorRepository).findById(authorId);
        verify(authorRepository).save(any(Author.class));
    }
}