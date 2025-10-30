//package pt.psoft.g1.psoftg1.lendingmanagement.repositories.elasticSearch;
//
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
//import pt.psoft.g1.psoftg1.lendingmanagement.model.elasticSearch.LendingES;
//import pt.psoft.g1.psoftg1.shared.services.Page;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//public interface LendingRepositoryES extends ElasticsearchRepository<LendingES, String> {
//    Optional<LendingES> findByLendingNumber(String lendingNumber);
//    List<LendingES> listByReaderNumberAndIsbn(String readerNumber, String isbn);
//    int getCountFromCurrentYear();
//    List<LendingES> listOutstandingByReaderNumber(String readerNumber);
//    Double getAverageDuration();
//    Double getAvgLendingDurationByIsbn(String isbn);
//
//
//    List<LendingES> getOverdue(Page page);
//    List<LendingES> searchLendings(Page page, String readerNumber, String isbn, Boolean returned, LocalDate startDate, LocalDate endDate);
//
//    LendingES save(LendingES lending);
//
//    void delete(LendingES lending);
//
//}
