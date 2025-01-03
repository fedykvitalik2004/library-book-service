package org.vitalii.fedyk.librarybookservice.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.vitalii.fedyk.librarybookservice.dto.BookGenreDto;
import org.vitalii.fedyk.librarybookservice.dto.CreateBookDto;
import org.vitalii.fedyk.librarybookservice.dto.ReadBookDto;
import org.vitalii.fedyk.librarybookservice.exception.NotFoundException;
import org.vitalii.fedyk.librarybookservice.exception.OperationNotPermittedException;
import org.vitalii.fedyk.librarybookservice.model.Book;
import org.vitalii.fedyk.librarybookservice.repository.BookRepository;
import org.vitalii.fedyk.librarybookservice.search.BookSpecification;
import org.vitalii.fedyk.librarybookservice.search.SpecificationsBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookServiceImplTest {
    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Sql("/data.sql")
    void testReadBook_Success() {
        assertNotNull(bookService.readBook(1L));
    }

    @Test
    @Sql("/data.sql")
    void testReadBook_NotFound() {
        assertThrows(NotFoundException.class, () -> bookService.readBook(1000L));
    }

    @Test
    @Sql("/data.sql")
    void testAuthorHasBooks() {
        assertTrue(bookService.authorHasBooks(1L));
    }

    @Test
    @Sql("/data.sql")
    void testDeleteBook_BookBorrowed() {
        final long booksBefore = bookRepository.count();
        assertThrows(OperationNotPermittedException.class, () -> bookService.deleteBook(1L));
        final long booksAfter = bookRepository.count();
        assertEquals(booksBefore, booksAfter);
    }

    @Test
    @Sql("/data.sql")
    void testDeleteBook_BookNotFound() {
        final long booksBefore = bookRepository.count();
        assertThrows(NotFoundException.class, () -> bookService.deleteBook(1000L));
        final long booksAfter = bookRepository.count();
    }

    @Test
    @Sql("/data.sql")
    void testDeleteBook_Success() {
        final long booksBefore = bookRepository.count();
        bookService.deleteBook(2L);
        final long booksAfter = bookRepository.count();
        assertEquals(1, booksBefore - booksAfter);
    }

    @Test
    @Sql("/data.sql")
    void testCreateBook_Success() {
        final long booksBefore = bookRepository.count();
        final CreateBookDto createBookDto = new CreateBookDto();
        createBookDto.setTitle("Book Title");
        createBookDto.setGenre(BookGenreDto.HORROR);
        createBookDto.setDescription("Book Description");
        createBookDto.setPagesCount(100);
        createBookDto.setAuthorId(1L);

        bookService.createBook(createBookDto);
        final long booksAfter = bookRepository.count();

        assertEquals(1, booksAfter - booksBefore);
    }

    @Test
    @Sql("/data.sql")
    void testCreateBook_AuthorNotExists() {
        final CreateBookDto createBookDto = new CreateBookDto();
        createBookDto.setTitle("BookTitle");
        createBookDto.setGenre(BookGenreDto.HISTORICAL);
        createBookDto.setDescription("BookDescription");
        createBookDto.setPagesCount(100);
        createBookDto.setAuthorId(1000L);

        assertThrows(IllegalArgumentException.class, () -> bookService.createBook(createBookDto));
    }

    @Test
    @Sql("/data.sql")
    void testSearch_Success() {
        final String title = "Title";
        final SpecificationsBuilder<Book> builder = new SpecificationsBuilder<>();
        builder.with("title", ":", title);

        final List<ReadBookDto> result = bookService.search(builder.build(BookSpecification::new));

        final long elementsInDatabase = bookRepository.countByTitle(title);
        assertEquals(elementsInDatabase, result.size());
    }

    @Test
    @Sql("/data.sql")
    void testSearch_NonExistentField() {
        final SpecificationsBuilder<Book> builder = new SpecificationsBuilder<>();
        builder.with("non_existent", ":", "Title");

        final List<ReadBookDto> result = bookService.search(builder.build(BookSpecification::new));

        final long allElements = bookRepository.count();
        assertEquals(allElements, result.size(), "If the field doesn't exist, then the result will contain all data");
    }

    @Test
    @Sql("/data.sql")
    void updateBook_Success() {
        final long bookId = 1L;
        final CreateBookDto createBookDto = new CreateBookDto();
        createBookDto.setTitle("Changed");
        createBookDto.setGenre(BookGenreDto.CHILDREN);
        createBookDto.setDescription("Changed");
        createBookDto.setPagesCount(900);
        createBookDto.setAuthorId(1L);

        final ReadBookDto result = bookService.updateBook(bookId, createBookDto);

        assertNotNull(result);
        assertEquals(bookId, result.getId());
        assertEquals(createBookDto.getTitle(), result.getTitle());
        assertEquals(createBookDto.getDescription(), result.getDescription());
        assertEquals(createBookDto.getPagesCount(), result.getPagesCount());
        assertEquals(createBookDto.getAuthorId(), result.getAuthorId());

        final Book book = bookRepository.findById(bookId).orElseThrow();
        assertEquals(createBookDto.getGenre().toString(), book.getGenre().toString());
        assertEquals(createBookDto.getAuthorId(), book.getAuthor().getId());
        assertEquals(createBookDto.getTitle(), book.getTitle());
        assertEquals(createBookDto.getDescription(), book.getDescription());
        assertEquals(createBookDto.getPagesCount(), book.getPagesCount());
    }

    @Test
    @Sql("/data.sql")
    void updateBook_AuthorNotFound() {
        final CreateBookDto createBookDto = new CreateBookDto();
        createBookDto.setTitle("Changed");
        createBookDto.setGenre(BookGenreDto.CHILDREN);
        createBookDto.setDescription("Changed");
        createBookDto.setPagesCount(900);
        createBookDto.setAuthorId(1000L);

        assertThrows(IllegalArgumentException.class, () -> bookService.updateBook(1L, createBookDto));
    }

    @Test
    @Sql("/data.sql")
    void updateBook_BookNotFound() {
        final CreateBookDto createBookDto = new CreateBookDto();
        createBookDto.setTitle("Changed");
        createBookDto.setGenre(BookGenreDto.CHILDREN);
        createBookDto.setDescription("Changed");
        createBookDto.setPagesCount(900);
        createBookDto.setAuthorId(1L);

        assertThrows(NotFoundException.class, () -> bookService.updateBook(1000L, createBookDto));
    }
}