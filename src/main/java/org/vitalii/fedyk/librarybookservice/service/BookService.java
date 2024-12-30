package org.vitalii.fedyk.librarybookservice.service;

import org.springframework.data.jpa.domain.Specification;
import org.vitalii.fedyk.librarybookservice.dto.ReadBookDto;
import org.vitalii.fedyk.librarybookservice.dto.CreateBookDto;
import org.vitalii.fedyk.librarybookservice.model.Book;

import java.util.List;

public interface BookService {
    ReadBookDto createBook(CreateBookDto createBookDto);

    ReadBookDto readBook(Long id);

    ReadBookDto updateBook(Long id, CreateBookDto createBookDto);

    void deleteBook(Long id);

    List<ReadBookDto> search(Specification<Book> specification);

    boolean authorHasBooks(Long authorId);
}
