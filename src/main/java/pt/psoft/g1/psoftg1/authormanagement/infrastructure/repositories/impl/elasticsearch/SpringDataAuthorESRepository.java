package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import pt.psoft.g1.psoftg1.authormanagement.model.elasticsearch.AuthorES;

import java.util.Optional;



public interface SpringDataAuthorESRepository
        extends ElasticsearchRepository<AuthorES, String> {

    Optional<AuthorES> findByAuthorNumber(Long authorNumber);

}