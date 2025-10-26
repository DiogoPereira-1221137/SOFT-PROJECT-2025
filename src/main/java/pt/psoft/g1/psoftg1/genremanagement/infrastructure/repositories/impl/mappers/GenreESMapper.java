package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.mappers;


import org.mapstruct.Mapper;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.model.elasticsearch.GenreES;

@Mapper(componentModel = "spring")
public interface GenreESMapper {

    GenreES toEntity(Genre model);

    Genre toModel(GenreES entity);
}