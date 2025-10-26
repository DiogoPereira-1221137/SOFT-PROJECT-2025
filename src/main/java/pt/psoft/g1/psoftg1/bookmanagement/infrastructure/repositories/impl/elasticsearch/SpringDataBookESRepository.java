package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.elasticsearch;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.NamedValue;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.AggregationsContainer;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.elasticsearch.BookES;
import pt.psoft.g1.psoftg1.bookmanagement.services.elasticsearch.BookESCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.lendingmanagement.model.elasticSearch.LendingES;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Profile("es")
public interface SpringDataBookESRepository extends
        ElasticsearchRepository<BookES, String>,
        BookRepoCustomES {

    Optional<BookES> findByIsbn(String isbn);

    List<BookES> findByGenreNameContaining(String genre);

    List<BookES> findByTitleContaining(String title);
}

interface BookRepoCustomES {
    List<BookESCountDTO> findTop5BooksLent(LocalDate oneYearAgo, Page page);
    List<BookES> findByAuthorName(String authorName);
    List<BookES> findBooksByAuthorNumber(Long authorNumber);
    List<BookES> searchBooks(Page page, SearchBooksQuery query);
}

@RequiredArgsConstructor
class BookRepoCustomESImpl implements BookRepoCustomES {

    private final ElasticsearchOperations operations;

    @Override
    public List<BookESCountDTO> findTop5BooksLent(LocalDate oneYearAgo, Page page) {
        Query query = QueryBuilders.range(r -> r
                .field("startDate")
                .gt(JsonData.of(oneYearAgo))
        );

        Aggregation bookAgg = new Aggregation.Builder()
                .terms(t -> t
                        .field("book.isbn.keyword")
                        .size(page.getLimit())
                        .order(NamedValue.of("_count", SortOrder.Desc))
                )
                .build();

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query)
                .withAggregation("books", bookAgg)
                .build();

        SearchHits<LendingES> hits = operations.search(nativeQuery, LendingES.class);

        AggregationsContainer<?> aggregations = hits.getAggregations();
        if (aggregations == null) return List.of();

        @SuppressWarnings("unchecked")
        Map<String, Aggregate> aggMap = (Map<String, Aggregate>) aggregations.aggregations();

        Aggregate bookAggregate = aggMap.get("books");
        if (bookAggregate == null || !bookAggregate.isSterms()) return List.of();

        List<BookESCountDTO> result = new ArrayList<>();

        for (StringTermsBucket bucket : bookAggregate.sterms().buckets().array()) {
            String isbn = bucket.key().stringValue();
            long count = bucket.docCount();

            // Buscar o livro pelo ISBN
            Optional<BookES> bookOpt = operations.search(
                    NativeQuery.builder()
                            .withQuery(QueryBuilders.term(t -> t.field("isbn.keyword").value(isbn)))
                            .build(),
                    BookES.class
            ).stream().findFirst().map(hit -> hit.getContent());

            if (bookOpt.isPresent()) {
                result.add(new BookESCountDTO(bookOpt.get(), count));
            }
        }

        return result;
    }

    @Override
    public List<BookES> findByAuthorName(String authorName) {
        // Query para procurar pelo nome do autor
        Query query = QueryBuilders.match(m -> m
                .field("authors.name")
                .query(authorName)
        );

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query)
                .build();

        SearchHits<BookES> hits = operations.search(nativeQuery, BookES.class);

        return hits.stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());
    }

    @Override
    public List<BookES> findBooksByAuthorNumber(Long authorNumber) {
        // Query para procurar pelo número do autor
        Query query = QueryBuilders.term(t -> t
                .field("authors.authorNumber")
                .value(authorNumber)
        );

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query)
                .build();

        SearchHits<BookES> hits = operations.search(nativeQuery, BookES.class);

        return hits.stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());
    }

    @Override
    public List<BookES> searchBooks(Page page, SearchBooksQuery query) {
        String title = query.getTitle();
        String genre = query.getGenre();
        String authorName = query.getAuthorName();

        // Construir query booleana com múltiplos critérios
        BoolQuery.Builder boolQuery = QueryBuilders.bool();

        if (title != null && !title.isEmpty()) {
            boolQuery.must(QueryBuilders.wildcard(w -> w
                    .field("title")
                    .value(title + "*")
            ));
        }

        if (genre != null && !genre.isEmpty()) {
            boolQuery.must(QueryBuilders.wildcard(w -> w
                    .field("genre")
                    .value(genre + "*")
            ));
        }

        if (authorName != null && !authorName.isEmpty()) {
            boolQuery.must(QueryBuilders.wildcard(w -> w
                    .field("authors.name")
                    .value(authorName + "*")
            ));
        }

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(boolQuery.build()._toQuery())
                .withPageable(org.springframework.data.domain.PageRequest.of(
                        page.getNumber() - 1,
                        page.getLimit(),
                        org.springframework.data.domain.Sort.by("title").ascending()
                ))
                .build();

        SearchHits<BookES> hits = operations.search(nativeQuery, BookES.class);

        return hits.stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());
    }
}