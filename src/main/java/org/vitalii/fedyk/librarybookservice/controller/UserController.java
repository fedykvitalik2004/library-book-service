package org.vitalii.fedyk.librarybookservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.vitalii.fedyk.librarybookservice.dto.ReadBorrowedBookDto;
import org.vitalii.fedyk.librarybookservice.service.BorrowedBookService;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController implements UsersApi {
    private BorrowedBookService borrowedBookService;

    @Override
    public List<ReadBorrowedBookDto> findBorrowedBooks(Long userId) {
        return borrowedBookService.getBorrowedBooksForUser(userId);
    }
}
