package pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl.elasticsearch;

import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.readermanagement.model.elasticsearch.ReaderDetailsES;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.shared.model.elasticsearch.PhotoES;

import java.util.Optional;

@Profile("es")
public interface SpringDataPhotoRepositoryES  extends ReactiveElasticsearchRepository<PhotoES, Long> {

    Optional<PhotoES> findByPhotoFile(String photoFile);

    void deleteByPhotoFile(String photoFile);

}
