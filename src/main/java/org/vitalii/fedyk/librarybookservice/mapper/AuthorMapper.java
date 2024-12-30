package org.vitalii.fedyk.librarybookservice.mapper;

import org.mapstruct.Mapper;
import org.vitalii.fedyk.librarybookservice.model.Author;
import org.vitalii.fedyk.librarybookservice.dto.ReadAuthorDto;
import org.vitalii.fedyk.librarybookservice.dto.CreateAuthorDto;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    Author toAuthor(CreateAuthorDto createAuthorDto);

    ReadAuthorDto toReadAuthorDto(Author author);
}