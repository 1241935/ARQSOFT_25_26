package pt.psoft.g1.psoftg1.authormanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pt.psoft.g1.psoftg1.authormanagement.model.Author;

@Mapper(componentModel = "spring")
public interface AuthorViewAMQPMapper {
    @Mapping(target = "photoURI", source = "photo.photoFile")
    AuthorViewAMQP toAuthorViewAMQP(Author author);
} 