package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.model.Description;
import pt.psoft.g1.psoftg1.bookmanagement.model.elasticsearch.BookES;
import pt.psoft.g1.psoftg1.bookmanagement.model.elasticsearch.IsbnES;
import pt.psoft.g1.psoftg1.bookmanagement.model.elasticsearch.DescriptionES;
import pt.psoft.g1.psoftg1.bookmanagement.model.elasticsearch.TitleES;

@Mapper(componentModel = "spring")
public interface BookESMapper {

    BookES toEntity(Book model);

    Book toModel(BookES entity);

    default IsbnES map(String isbn) {
        return (isbn != null) ? new IsbnES(isbn) : null;
    }

    default String map(IsbnES isbnES) {
        return (isbnES != null) ? isbnES.toString() : null;
    }

    default DescriptionES mapDescription(String description) {
        return (description != null) ? new DescriptionES(description) : null;
    }

    default String mapDescription(DescriptionES descriptionES) {
        return (descriptionES != null) ? descriptionES.toString() : null;
    }

    default TitleES mapTitle(String title) {
        return (title != null) ? new TitleES(title) : null;
    }

    default String mapTitle(TitleES titleES) {
        return (titleES != null) ? titleES.toString() : null;
    }
}

