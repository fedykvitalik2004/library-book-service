package org.vitalii.fedyk.librarybookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.vitalii.fedyk.librarybookservice.model.BorrowedBook;
import org.vitalii.fedyk.librarybookservice.model.BorrowedBookId;

import java.util.List;

@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, BorrowedBookId> {
    boolean existsByBorrowedBookIdUserId(long userId);

    boolean existsByBorrowedBookIdBookId(long bookId);

    @Query("SELECT bb " +
           "FROM BorrowedBook bb " +
           "JOIN FETCH Book b ON bb.borrowedBookId.bookId = b.id " +
           "WHERE bb.borrowedBookId.userId = :userId")
    List<BorrowedBook> findByUserId(long userId);
}