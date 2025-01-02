package org.vitalii.fedyk.librarybookservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.vitalii.fedyk.librarybookservice.dto.CreateBorrowedBookDto;
import org.vitalii.fedyk.librarybookservice.dto.ReadBorrowedBookDto;
import org.vitalii.fedyk.librarybookservice.service.BorrowedBookService;

import java.util.List;

@RestController
@AllArgsConstructor
public class BorrowedBookController implements BorrowedBooksApi {
    private BorrowedBookService borrowedBookService;

    @Override
    public ReadBorrowedBookDto addBorrowedBook(CreateBorrowedBookDto createBorrowedBookDto) {
        return borrowedBookService.add(createBorrowedBookDto);
    }

    @Override
    public void deleteBorrowedBook(Long bookId, Long userId) {
        borrowedBookService.remove(bookId, userId);
    }

    @Override
    public Boolean isBorrowedByUser(Long userId) {
        return borrowedBookService.isBorrowedByUser(userId);
    }

    @Override
    public List<ReadBorrowedBookDto> findBorrowedBooks(Long userId) {
        return borrowedBookService.getBorrowedBooksForUser(userId);
    }
}