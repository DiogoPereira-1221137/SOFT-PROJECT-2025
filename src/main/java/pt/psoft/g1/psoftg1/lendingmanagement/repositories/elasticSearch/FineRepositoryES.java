//package pt.psoft.g1.psoftg1.lendingmanagement.repositories.elasticSearch;
//
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
//import pt.psoft.g1.psoftg1.lendingmanagement.model.elasticSearch.FineES;
//import pt.psoft.g1.psoftg1.lendingmanagement.model.elasticSearch.LendingES;
//
//import java.util.Optional;
//
//public interface FineRepositoryES extends ElasticsearchRepository<FineES, String> {
//
//    Optional<FineES> findByLendingNumber(String lendingNumber);
//    Iterable<FineES> findAll();
//
//    Fine save(Fine fine);
//
//}
