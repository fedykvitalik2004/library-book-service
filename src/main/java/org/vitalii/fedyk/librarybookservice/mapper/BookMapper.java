package org.vitalii.fedyk.librarybookservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vitalii.fedyk.librarybookservice.model.Book;
import org.vitalii.fedyk.librarybookservice.dto.CreateBookDto;
import org.vitalii.fedyk.librarybookservice.dto.ReadBookDto;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mapping(target = "author.id", source = "authorId")
    Book toBook(CreateBookDto createBookDto);
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorFullName", source = "author.fullName")
    ReadBookDto toReadBookDto(Book book);
}