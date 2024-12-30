package org.vitalii.fedyk.librarybookservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.vitalii.fedyk.librarybookservice.exception.NotFoundException;
import org.vitalii.fedyk.librarybookservice.exception.OperationNotPermittedException;
import org.vitalii.fedyk.librarybookservice.mapper.AuthorMapper;
import org.vitalii.fedyk.librarybookservice.model.Author;
import org.vitalii.fedyk.librarybookservice.model.FullName;
import org.vitalii.fedyk.librarybookservice.repository.AuthorRepository;
import org.vitalii.fedyk.librarybookservice.service.AuthorService;
import org.vitalii.fedyk.librarybookservice.service.BookService;
import org.vitalii.fedyk.librarybookservice.dto.CreateAuthorDto;
import org.vitalii.fedyk.librarybookservice.dto.ReadAuthorDto;
import org.vitalii.fedyk.librarybookservice.dto.ReadAuthorsDto;
import org.vitalii.fedyk.librarybookservice.dto.PaginationDto;

import static org.vitalii.fedyk.librarybookservice.constant.ExceptionConstants.AUTHOR_CANNOT_BE_DELETED;
import static org.vitalii.fedyk.librarybookservice.constant.ExceptionConstants.AUTHOR_NOT_FOUND_BY_ID;

@Service
@Transactional
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private BookService bookService;
    private AuthorMapper authorMapper;
    private AuthorRepository authorRepository;

    @Override
    public ReadAuthorDto createAuthor(final CreateAuthorDto createAuthorDto) {
        final Author author = authorMapper.toAuthor(createAuthorDto);
        return authorMapper.toReadAuthorDto(authorRepository.save(author));
    }

    @Override
    public ReadAuthorDto readAuthor(final Long id) {
        return authorMapper.toReadAuthorDto(authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AUTHOR_NOT_FOUND_BY_ID.formatted(id))));
    }

    @Override
    public ReadAuthorDto updateAuthor(final Long id, final CreateAuthorDto createAuthorDto) {
        final Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AUTHOR_NOT_FOUND_BY_ID.formatted(id)));
        author.setFullName(new FullName(createAuthorDto.getFullName().getFirstName(),
                createAuthorDto.getFullName().getLastName()));
        author.setDescription(createAuthorDto.getDescription());
        return authorMapper.toReadAuthorDto(authorRepository.save(author));
    }

    @Override
    public void deleteAuthor(final Long id) {
        if (!authorRepository.existsById(id)) {
            throw new NotFoundException(AUTHOR_NOT_FOUND_BY_ID.formatted(id));
        }
        if (bookService.authorHasBooks(id)) {
            throw new OperationNotPermittedException(AUTHOR_CANNOT_BE_DELETED);
        }
        authorRepository.deleteById(id);
    }

    @Override
    public ReadAuthorsDto findAll(final Pageable pageable) {
        return createAuthorsDto(authorRepository.findAll(pageable));
    }

    private ReadAuthorsDto createAuthorsDto(final Page<Author> authors) {
        ReadAuthorsDto readAuthorsDto = new ReadAuthorsDto();
        readAuthorsDto.setAuthors(authors.getContent().stream().map(authorMapper::toReadAuthorDto).toList());
        final org.vitalii.fedyk.librarybookservice.dto.PaginationDto paginationDto = new PaginationDto();
        paginationDto.setPageNumber(authors.getNumber());
        paginationDto.setPageSize(authors.getSize());
        paginationDto.setTotalNumberOfPages(authors.getTotalPages());
        readAuthorsDto.setPagination(paginationDto);
        return readAuthorsDto;
    }
}