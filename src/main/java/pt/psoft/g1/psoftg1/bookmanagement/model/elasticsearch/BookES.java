package pt.psoft.g1.psoftg1.bookmanagement.model.elasticsearch;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Document(indexName = "books")
public class BookES {

    @Id
    @Getter @Setter
    private String id; // Elasticsearch usa String id

    @Field(type = FieldType.Object)
    @Getter @Setter
    private IsbnES isbn;

    @Field(type = FieldType.Object)
    @Getter @Setter
    private TitleES title;

    @Field(type = FieldType.Object)
    @Getter @Setter
    private DescriptionES description;

    @Field(type = FieldType.Keyword) // armazenar apenas o nome do gênero
    @Getter @Setter
    private String genreName;

    @Field(type = FieldType.Keyword) // armazenar apenas os nomes dos autores
    @Getter @Setter
    private List<String> authorNames = new ArrayList<>();

    @Field(type = FieldType.Keyword)
    @Getter @Setter
    private String photoUri;

    public BookES(String isbn, String title, String description, String genreName, List<String> authorNames, String photoUri) {
        this.isbn = new IsbnES(isbn);
        this.title = new TitleES(title);
        this.description = (description != null) ? new DescriptionES(description) : null;

        if (genreName == null || genreName.isBlank())
            throw new IllegalArgumentException("Genre cannot be null");
        this.genreName = genreName;

        if (authorNames == null || authorNames.isEmpty())
            throw new IllegalArgumentException("Author list cannot be empty");
        this.authorNames = authorNames;

        this.photoUri = photoUri;
    }

    public String getIsbn() {
        return this.isbn.toString();
    }

    public String getTitle() {
        return this.title.toString();
    }

    public String getDescription() {
        return (this.description != null) ? this.description.toString() : null;
    }
}
