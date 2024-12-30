package org.vitalii.fedyk.librarybookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.vitalii.fedyk.librarybookservice.model.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query("SELECT b " +
           "FROM Book b " +
           "JOIN FETCH b.author " +
           "WHERE b.id IN :ids")
    List<Book> findAllByIds(List<Long> ids);
    boolean existsByAuthorId(Long authorId);

    long countByTitle(String title);
}