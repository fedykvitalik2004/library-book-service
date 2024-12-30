package org.vitalii.fedyk.librarybookservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vitalii.fedyk.librarybookservice.model.Book;
import org.vitalii.fedyk.librarybookservice.model.BorrowedBook;
import org.vitalii.fedyk.librarybookservice.dto.ReadBorrowedBookDto;

@Mapper(componentModel = "spring")
public interface BorrowedBookMapper {
    @Mapping(target = "borrowedBookId.userId", source = "userId")
    @Mapping(target = "borrowedBookId.bookId", source = "bookId")
    BorrowedBook toBorrowedBook(org.vitalii.fedyk.librarybookservice.dto.CreateBorrowedBookDto createBorrowedBookDto);
    @Mapping(target = "book.authorId", source = "book.author.id")
    @Mapping(target = "book.authorFullName", source = "book.author.fullName")
    @Mapping(target = "book.genre", source = "book.genre")
    @Mapping(target = "book.pagesCount", source = "book.pagesCount")
    @Mapping(target = "book.title", source = "book.title")
    @Mapping(target = "book.id", source = "book.id")
    @Mapping(target = "userId", source = "borrowedBook.borrowedBookId.userId")
    ReadBorrowedBookDto toBorrowedBookDto(BorrowedBook borrowedBook, Book book);
}