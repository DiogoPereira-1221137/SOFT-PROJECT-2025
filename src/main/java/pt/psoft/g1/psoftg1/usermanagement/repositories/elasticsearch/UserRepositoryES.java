package pt.psoft.g1.psoftg1.usermanagement.repositories.elasticsearch;

import java.util.List;
import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import pt.psoft.g1.psoftg1.usermanagement.model.elasticsearch.UserES;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.usermanagement.services.SearchUsersQuery;


public interface UserRepositoryES extends ElasticsearchRepository<UserES, String> {

    <S extends UserES> S save(S entity);

    <S extends UserES> Iterable<S> saveAll(Iterable<S> entities);

    Optional<UserES> findById(String id);

    Optional<UserES> findByUsername(String username);

    List<UserES> findByName(String name);

    List<UserES> findByNameContaining(String name);

    void delete(UserES entity);

}
