package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl.elasticsearch;

import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.model.elasticSearch.LendingES;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Profile("es")
public interface SpringDataLendingESRepository extends ElasticsearchRepository<LendingES, String> {

    /**
     * Find lending by lending number.
     */
    Optional<LendingES> findByLendingNumber_LendingNumber(String lendingNumber);

    /**
     * List all lendings from a specific reader and book (via ISBN).
     */
    List<LendingES> findByReaderDetails_ReaderNumber_ReaderNumberAndBook_Isbn_Isbn(
            String readerNumber, String isbn);

    /**
     * Count all lendings from the current year.
     * (You'll likely need to filter on the client side or via a custom query.)
     */
    List<LendingES> findByStartDateBetween(LocalDate start, LocalDate end);

    /**
     * List outstanding lendings (not yet returned).
     */
    List<LendingES> findByReaderDetails_ReaderNumber_ReaderNumberAndReturnedDateIsNull(String readerNumber);

    /**
     * List lendings by return status.
     */
    List<LendingES> findByReturnedDateIsNull();

    List<LendingES> findByReturnedDateIsNotNull();
}