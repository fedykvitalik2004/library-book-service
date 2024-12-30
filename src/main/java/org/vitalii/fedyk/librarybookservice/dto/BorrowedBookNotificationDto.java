package org.vitalii.fedyk.librarybookservice.dto;

public class BorrowedBookNotificationDto {
    private String firstName;
    private String bookTitle;
    private String email;

    public BorrowedBookNotificationDto() {
    }

    public BorrowedBookNotificationDto(String firstName, String bookTitle, String email) {
        this.firstName = firstName;
        this.bookTitle = bookTitle;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
