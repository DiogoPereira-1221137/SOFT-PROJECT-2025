package pt.psoft.g1.psoftg1.readermanagement.repositories.elasticsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.readermanagement.model.elasticsearch.ReaderDetailsES;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReaderRepositoryES {
    Optional<ReaderDetailsES> findByReaderNumber(String readerNumber);
    List<ReaderDetailsES> findByPhoneNumber(String phoneNumber);
    Optional<ReaderDetailsES> findByReader_Username(String username);
//    Optional<ReaderDetailsES> findByReader_UserId(Long userId);

    Page<ReaderDetailsES> findTopReaders(Pageable pageable);
    Page<ReaderBookCountDTO> findTopByGenre(Pageable pageable, String genre, LocalDate startDate, LocalDate endDate);
}