package org.vitalii.fedyk.librarybookservice.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.vitalii.fedyk.librarybookservice.client.UserApi;
import org.vitalii.fedyk.librarybookservice.dto.BorrowedBookNotificationDto;
import org.vitalii.fedyk.librarybookservice.dto.ReadUserDto;
import org.vitalii.fedyk.librarybookservice.exception.NotFoundException;
import org.vitalii.fedyk.librarybookservice.exception.OperationNotPermittedException;
import org.vitalii.fedyk.librarybookservice.mapper.BorrowedBookMapper;
import org.vitalii.fedyk.librarybookservice.model.Book;
import org.vitalii.fedyk.librarybookservice.model.BorrowedBook;
import org.vitalii.fedyk.librarybookservice.model.BorrowedBookId;
import org.vitalii.fedyk.librarybookservice.producer.DefaultServiceEventsProducer;
import org.vitalii.fedyk.librarybookservice.producer.IDefaultServiceEventsProducer;
import org.vitalii.fedyk.librarybookservice.repository.BookRepository;
import org.vitalii.fedyk.librarybookservice.repository.BorrowedBookRepository;
import org.vitalii.fedyk.librarybookservice.service.BorrowedBookService;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.vitalii.fedyk.librarybookservice.dto.ReadBorrowedBookDto;
import org.vitalii.fedyk.librarybookservice.dto.CreateBorrowedBookDto;

import static org.vitalii.fedyk.librarybookservice.constant.ExceptionConstants.*;

@Service
@AllArgsConstructor
public class BorrowedBookServiceImpl implements BorrowedBookService {
    private UserApi userClient;
    private BookRepository bookRepository;
    private BorrowedBookRepository borrowedBookRepository;
    private BorrowedBookMapper borrowedBookMapper;
    private DefaultServiceEventsProducer producer;

    @Override
    public ReadBorrowedBookDto add(CreateBorrowedBookDto createBorrowedBookDto) {
        if (borrowedBookRepository.existsById(new BorrowedBookId(createBorrowedBookDto.getBookId(),
                createBorrowedBookDto.getUserId()))) {
            throw new OperationNotPermittedException(BORROWED_BOOK_ALREADY_EXISTS);
        }
        final BorrowedBook borrowedBook = borrowedBookMapper.toBorrowedBook(createBorrowedBookDto);
        final Book book = bookRepository.findById(createBorrowedBookDto.getBookId())
                .orElseThrow(() -> new IllegalArgumentException(
                        BOOK_NOT_FOUND_BY_ID.formatted(createBorrowedBookDto.getBookId())));
        if (!userClient.existsById(createBorrowedBookDto.getUserId())) {
            throw new IllegalArgumentException(USER_NOT_FOUND_BY_ID.formatted(createBorrowedBookDto.getUserId()));
        }
        borrowedBook.setBorrowDate(ZonedDateTime.now());
        producer.onBorrowedBookNotification(
                createBorrowedNotificationDto(createBorrowedBookDto.getUserId(), book.getTitle()),
                new IDefaultServiceEventsProducer.BorrowedBookNotificationDtoHeaders()
        );
        return borrowedBookMapper.toBorrowedBookDto(borrowedBookRepository.save(borrowedBook), book);
    }

    private BorrowedBookNotificationDto createBorrowedNotificationDto(long userId, String bookTitle) {
        final ReadUserDto readUserDto = userClient.getUserById(userId);
        assert readUserDto.getFullName() != null;
        return new BorrowedBookNotificationDto()
                .withFirstName(readUserDto.getFullName().getFirstName())
                .withBookTitle(bookTitle)
                .withEmail(readUserDto.getEmail());

    }

    @Override
    public boolean isBorrowedByUser(final Long userId) {
        return borrowedBookRepository.existsByBorrowedBookIdUserId(userId);
    }

    @Override
    public boolean isBorrowedByBook(final Long bookId) {
        return borrowedBookRepository.existsByBorrowedBookIdBookId(bookId);
    }

    @Override
    public void remove(final Long bookId, final Long userId) {
        final BorrowedBook borrowedBook = borrowedBookRepository.findById(new BorrowedBookId(bookId, userId))
                .orElseThrow(() -> new NotFoundException(BORROWED_BOOK_NOT_FOUND));
        borrowedBookRepository.delete(borrowedBook);
    }

    @Override
    public List<ReadBorrowedBookDto> getBorrowedBooksForUser(final Long userId) {
        final List<BorrowedBook> borrowedBooks = borrowedBookRepository.findByUserId(userId)
                .stream().toList();
        final List<Book> books = bookRepository.findAllByIds(borrowedBooks.stream()
                .map(book -> book.getBorrowedBookId().getBookId()).toList());
        final List<ReadBorrowedBookDto> readBorrowedBooks = new ArrayList<>();
        for (int i = 0; i < borrowedBooks.size(); i++) {
            readBorrowedBooks.add(borrowedBookMapper.toBorrowedBookDto(borrowedBooks.get(i), books.get(i)));
        }
        return readBorrowedBooks;
    }
}