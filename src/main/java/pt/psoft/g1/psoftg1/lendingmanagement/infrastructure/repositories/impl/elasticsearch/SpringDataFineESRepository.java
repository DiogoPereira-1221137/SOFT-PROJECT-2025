package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl.elasticsearch;

import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.model.elasticSearch.FineES;

import java.util.Optional;

@Profile("es")
public interface SpringDataFineESRepository extends ElasticsearchRepository<FineES, String> {

    /**
     * Find a fine document by lending number.
     * In Elasticsearch, nested fields can be queried with property paths.
     * Example: lending.lendingNumber.lendingNumber
     */
    Optional<FineES> findByLending_LendingNumber_LendingNumber(String lendingNumber);
}
