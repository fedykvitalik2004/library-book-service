package org.vitalii.fedyk.librarybookservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import org.vitalii.fedyk.librarybookservice.dto.CreateAuthorDto;
import org.vitalii.fedyk.librarybookservice.dto.ReadAuthorDto;
import org.vitalii.fedyk.librarybookservice.dto.ReadAuthorsDto;
import org.vitalii.fedyk.librarybookservice.service.AuthorService;

@RestController
@AllArgsConstructor
public class AuthorController implements AuthorsApi {
    private AuthorService authorService;

    @Override
    public ReadAuthorDto createAuthor(CreateAuthorDto createAuthorDto) {
        return authorService.createAuthor(createAuthorDto);
    }

    @Override
    public void deleteAuthor(Long id) {
        authorService.deleteAuthor(id);
    }

    @Override
    public ReadAuthorsDto getAllAuthors(Pageable pageable) {
        return authorService.findAll(pageable);
    }

    @Override
    public ReadAuthorDto getAuthorById(Long id) {
        return authorService.readAuthor(id);
    }

    @Override
    public ReadAuthorDto updateAuthor(Long id, CreateAuthorDto createAuthorDto) {
        return authorService.updateAuthor(id, createAuthorDto);
    }
}
