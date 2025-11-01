package pt.psoft.g1.psoftg1.authormanagement.repositories.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.elasticsearch.AuthorES;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.mappers.AuthorESMapper;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("es")
@RequiredArgsConstructor
public class AuthorESRepository implements AuthorRepository {

    private final ElasticsearchClient client;
    private final AuthorESMapper mapper;

    private static final String INDEX = "authors";

    @Override
    public Optional<Author> findByAuthorNumber(Long authorNumber) {
        try {
            SearchResponse<AuthorES> response = client.search(s -> s
                            .index(INDEX)
                            .query(q -> q.term(t -> t.field("authorNumber").value(authorNumber.toString()))),
                    AuthorES.class
            );

            return response.hits().hits().stream()
                    .findFirst()
                    .map(Hit::source)
                    .map(mapper::toModel);
        } catch (IOException e) {
            throw new RuntimeException("Failed to search author by number in Elasticsearch", e);
        }
    }

    @Override
    public List<Author> searchByNameNameStartsWith(String name) {
        try {
            SearchResponse<AuthorES> response = client.search(s -> s
                            .index(INDEX)
                            .query(q -> q
                                    .prefix(p -> p.field("name.name").value(name.toLowerCase()))
                            ),
                    AuthorES.class
            );

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .map(mapper::toModel)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to search authors by name prefix", e);
        }
    }

    @Override
    public List<Author> searchByNameName(String name) {
        try {
            SearchResponse<AuthorES> response = client.search(s -> s
                            .index(INDEX)
                            .query(q -> q
                                    .match(m -> m.field("name.name").query(name))
                            ),
                    AuthorES.class
            );

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .map(mapper::toModel)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to search authors by name", e);
        }
    }

    @Override
    public Author save(Author author) {
        AuthorES authorES = mapper.toEntity(author);
        try {
            client.index(IndexRequest.of(i -> i
                    .index(INDEX)
                    .id(String.valueOf(author.getAuthorNumber()))
                    .document(authorES)
            ));
            return author;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save author in Elasticsearch", e);
        }
    }

    @Override
    public Iterable<Author> findAll() {
        try {
            SearchResponse<AuthorES> response = client.search(s -> s
                            .index(INDEX)
                            .size(1000)
                            .sort(sort -> sort.field(f -> f.field("name.name.keyword").order(SortOrder.Asc))),
                    AuthorES.class
            );

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .map(mapper::toModel)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch all authors from Elasticsearch", e);
        }
    }

    @Override
    public Page<AuthorLendingView> findTopAuthorByLendings(Pageable pageable) {
        // Elasticsearch não tem acesso direto aos dados de Lendings.
        // Este método só é suportado pela base de dados relacional.
        return Page.empty(pageable);
    }

    @Override
    public void delete(Author author) {
        try {
            client.delete(DeleteRequest.of(d -> d
                    .index(INDEX)
                    .id(String.valueOf(author.getAuthorNumber()))
            ));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete author in Elasticsearch", e);
        }
    }

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(Long authorNumber) {
        // Tal como findTopAuthorByLendings, esta query depende de relações (Book-Author)
        // que não estão representadas no índice ES.
        return List.of();
    }
}
