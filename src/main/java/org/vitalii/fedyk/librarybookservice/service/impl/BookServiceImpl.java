package org.vitalii.fedyk.librarybookservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.vitalii.fedyk.librarybookservice.exception.NotFoundException;
import org.vitalii.fedyk.librarybookservice.exception.OperationNotPermittedException;
import org.vitalii.fedyk.librarybookservice.mapper.BookMapper;
import org.vitalii.fedyk.librarybookservice.model.Author;
import org.vitalii.fedyk.librarybookservice.model.Book;
import org.vitalii.fedyk.librarybookservice.model.BookGenre;
import org.vitalii.fedyk.librarybookservice.repository.AuthorRepository;
import org.vitalii.fedyk.librarybookservice.repository.BookRepository;
import org.vitalii.fedyk.librarybookservice.service.BookService;
import org.vitalii.fedyk.librarybookservice.service.BorrowedBookService;
import org.vitalii.fedyk.librarybookservice.dto.CreateBookDto;

import org.vitalii.fedyk.librarybookservice.dto.ReadBookDto;

import java.util.List;

import static org.vitalii.fedyk.librarybookservice.constant.ExceptionConstants.*;

@Service
@Transactional
@AllArgsConstructor
public class BookServiceImpl implements BookService {
    private AuthorRepository authorRepository;
    private BorrowedBookService borrowedBookService;
    private BookRepository bookRepository;
    private BookMapper bookMapper;

    @Override
    public ReadBookDto createBook(final CreateBookDto createBookDto) {
        final Author author = authorRepository.findById(createBookDto.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException(AUTHOR_NOT_FOUND_BY_ID
                        .formatted(createBookDto.getAuthorId())));
        Book book = bookMapper.toBook(createBookDto);
        book.setAuthor(author);
        return bookMapper.toReadBookDto(bookRepository.save(book));
    }

    @Override
    public ReadBookDto readBook(final Long id) {
        return bookMapper.toReadBookDto(bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BOOK_NOT_FOUND_BY_ID.formatted(id))));
    }

    @Override
    public ReadBookDto updateBook(final Long id, final CreateBookDto createBookDto) {
        final Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BOOK_NOT_FOUND_BY_ID.formatted(id)));
        book.setTitle(createBookDto.getTitle());
        book.setDescription(createBookDto.getDescription());
        book.setGenre(BookGenre.valueOf(createBookDto.getGenre().getValue()));
        book.setPagesCount(createBookDto.getPagesCount());
        if(!book.getAuthor().getId().equals(createBookDto.getAuthorId())) {
            final Author author = authorRepository.findById(createBookDto.getAuthorId())
                    .orElseThrow(() -> new IllegalArgumentException(AUTHOR_NOT_FOUND_BY_ID
                            .formatted(createBookDto.getAuthorId())));
            book.setAuthor(author);
        }
        return bookMapper.toReadBookDto(book);
    }

    @Override
    public void deleteBook(final Long id) {
        if (borrowedBookService.isBorrowedByBook(id)) {
            throw new OperationNotPermittedException(BOOK_CANNOT_BE_DELETED);
        }
        final Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BOOK_NOT_FOUND_BY_ID.formatted(id)));
        bookRepository.delete(book);
    }

    @Override
    public List<ReadBookDto> search(final Specification<Book> specification) {
        return bookRepository.findAll(specification).stream().map(o -> bookMapper.toReadBookDto(o))
                .toList();
    }

    @Override
    public boolean authorHasBooks(Long authorId) {
        return bookRepository.existsByAuthorId(authorId);
    }
}
