package org.vitalii.fedyk.librarybookservice.service;

import java.util.List;

import org.vitalii.fedyk.librarybookservice.dto.ReadBorrowedBookDto;
import org.vitalii.fedyk.librarybookservice.dto.CreateBorrowedBookDto;

public interface BorrowedBookService {
    ReadBorrowedBookDto add(CreateBorrowedBookDto createBorrowedBookDto);

    boolean isBorrowedByUser(Long userId);

    boolean isBorrowedByBook(Long bookId);

    void remove(Long bookId, Long userId);

    List<ReadBorrowedBookDto> getBorrowedBooksForUser(Long userId);
}
