package pt.psoft.g1.psoftg1.bookmanagement.services.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.elasticsearch.BookES;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookESCountDTO {
    private BookES book;
    private long lendingCount;
}

