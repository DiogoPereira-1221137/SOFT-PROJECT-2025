package pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl.elasticsearch;

import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import pt.psoft.g1.psoftg1.shared.model.elasticsearch.PhotoES;

import java.util.Optional;

@Profile("es")
public interface SpringDataPhotoESRepository extends ElasticsearchRepository<PhotoES, Long> {

    Optional<PhotoES> findByPhotoFile(String photoFile);

    void deleteByPhotoFile(String photoFile);

}
