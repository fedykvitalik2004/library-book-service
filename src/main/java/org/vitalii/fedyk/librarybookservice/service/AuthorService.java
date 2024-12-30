package org.vitalii.fedyk.librarybookservice.service;

import org.springframework.data.domain.Pageable;
import org.vitalii.fedyk.librarybookservice.dto.ReadAuthorDto;
import org.vitalii.fedyk.librarybookservice.dto.ReadAuthorsDto;
import org.vitalii.fedyk.librarybookservice.dto.CreateAuthorDto;

public interface AuthorService {
    ReadAuthorDto createAuthor(CreateAuthorDto createAuthorDto);

    ReadAuthorDto readAuthor(Long id);

    ReadAuthorDto updateAuthor(Long id, CreateAuthorDto readAuthorDto);

    void deleteAuthor(Long id);

    ReadAuthorsDto findAll(Pageable pageable);
}
