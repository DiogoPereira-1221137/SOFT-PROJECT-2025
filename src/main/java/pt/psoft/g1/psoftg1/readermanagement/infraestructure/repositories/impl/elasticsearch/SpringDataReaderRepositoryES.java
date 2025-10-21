package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl.elasticsearch;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.readermanagement.model.elasticsearch.ReaderDetailsES;
import pt.psoft.g1.psoftg1.readermanagement.services.SearchReadersQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface SpringDataReaderRepositoryES extends
        ReactiveElasticsearchRepository<ReaderDetailsES, String>,
        ReaderDetailsRepoCustomES {

    Optional<ReaderDetailsES> findByReaderNumber(String readerNumber);
    List<ReaderDetailsES> findByPhoneNumber(String phoneNumber);
    Optional<ReaderDetailsES> findByReader_Username(String username);
//    Optional<ReaderDetailsES> findByReader_UserId(Long userId);
}





interface ReaderDetailsRepoCustomES {
    List<ReaderDetailsES> searchReaderDetails(SearchReadersQuery query, pt.psoft.g1.psoftg1.shared.services.Page page);
}

@RequiredArgsConstructor
 class ReaderDetailsRepoCustomESImpl implements ReaderDetailsRepoCustomES {

    private final ElasticsearchOperations operations;

    public List<ReaderDetailsES> searchReaderDetails(SearchReadersQuery query, pt.psoft.g1.psoftg1.shared.services.Page page) {
        Criteria criteria = new Criteria();

        List<Criteria> filters = new ArrayList<>();
        if (StringUtils.hasText(query.getName())) {
            filters.add(new Criteria("reader.name").contains(query.getName()));
        }
        if (StringUtils.hasText(query.getEmail())) {
            filters.add(new Criteria("reader.username").is(query.getEmail()));
        }
        if (StringUtils.hasText(query.getPhoneNumber())) {
            filters.add(new Criteria("phoneNumber").is(query.getPhoneNumber()));
        }

        if (!filters.isEmpty()) {
            criteria = filters.stream().reduce(Criteria::or).orElse(criteria);
        }

        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);
        criteriaQuery.setPageable(PageRequest.of(page.getNumber() - 1, page.getLimit()));

        SearchHits<ReaderDetailsES> hits = operations.search(criteriaQuery, ReaderDetailsES.class);
        return hits.stream().map(SearchHit::getContent).toList();
    }


}



