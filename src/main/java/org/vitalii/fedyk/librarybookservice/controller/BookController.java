package org.vitalii.fedyk.librarybookservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import org.vitalii.fedyk.librarybookservice.dto.CreateBookDto;
import org.vitalii.fedyk.librarybookservice.dto.ReadBookDto;
import org.vitalii.fedyk.librarybookservice.model.Book;
import org.vitalii.fedyk.librarybookservice.search.BookSpecification;
import org.vitalii.fedyk.librarybookservice.search.SpecificationsBuilder;
import org.vitalii.fedyk.librarybookservice.service.BookService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@AllArgsConstructor
public class BookController implements BooksApi {
    private BookService bookService;
    @Override
    public ReadBookDto createBook(CreateBookDto createBookDto) {
        return bookService.createBook(createBookDto);
    }

    @Override
    public void deleteBook(Long id) {
        bookService.deleteBook(id);
    }

    @Override
    public ReadBookDto getBookById(Long id) {
        return bookService.readBook(id);
    }

    @Override
    public List<ReadBookDto> search(String query, Pageable pageable) {
        final SpecificationsBuilder<Book> builder = new SpecificationsBuilder<>();
        final Pattern pattern = Pattern.compile("(\\w+?)(:|!:|<|>|<=|>=|contains)(\\w+?),");
        final Matcher matcher = pattern.matcher(query + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }
        return bookService.search(builder.build(BookSpecification::new));
    }

    @Override
    public ReadBookDto updateBook(Long id, CreateBookDto createBookDto) {
        return bookService.updateBook(id, createBookDto);
    }
}
