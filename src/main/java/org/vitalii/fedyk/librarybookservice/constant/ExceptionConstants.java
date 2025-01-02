package org.vitalii.fedyk.librarybookservice.constant;

public class ExceptionConstants {
    public static final String AUTHOR_NOT_FOUND_BY_ID = "Author not found by id %d";
    public static final String BOOK_NOT_FOUND_BY_ID = "Book not found by id %d";
    public static final String USER_NOT_FOUND_BY_ID = "User not found by id %d";
    public static final String BOOK_CANNOT_BE_DELETED = "You can't delete the book, because the book is borrowed";
    public static final String SEARCH_OPERATION_NOT_FOUND = "The search operation doesn't exist";
    public static final String BORROWED_BOOK_NOT_FOUND = "Borrowed book not found";
    public static final String BORROWED_BOOK_ALREADY_EXISTS = "Borrowed book already exists";
    public static final String AUTHOR_CANNOT_BE_DELETED = "Author cannot be deleted, because there are books of his. " +
                                                          "First delete books.";
}