package org.vitalii.fedyk.librarybookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vitalii.fedyk.librarybookservice.model.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}