package pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl.elasticsearch;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import pt.psoft.g1.psoftg1.usermanagement.model.elasticsearch.UserES;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;
import pt.psoft.g1.psoftg1.usermanagement.services.SearchUsersQuery;

/**
 * Elasticsearch equivalent of SpringDataUserRepository
 */
@Profile("es")
@CacheConfig(cacheNames = "users_es")
public interface SpringDataUserESRepository
        extends ElasticsearchRepository<UserES, String>, UserESRepoCustom {

    @Override
    @CacheEvict(allEntries = true)
    <S extends UserES> Iterable<S> saveAll(Iterable<S> entities);

    @Override
    @Caching(evict = {
            @CacheEvict(key = "#p0.id", condition = "#p0.id != null"),
            @CacheEvict(key = "#p0.username", condition = "#p0.username != null")
    })
    <S extends UserES> S save(S entity);

    @Override
    @Cacheable
    Optional<UserES> findById(String id);

    @Cacheable
    Optional<UserES> findByUsername(String username);

    @Cacheable
    List<UserES> findByName(String name);

    @Cacheable
    List<UserES> findByNameContaining(String name);
}

/**
 * Custom interface for Elasticsearch custom queries.
 */
interface UserESRepoCustom {
    List<UserES> searchUsers(Page page, SearchUsersQuery query);
}

/**
 * Custom implementation using ElasticsearchOperations (Native Query).
 */
@Repository
@RequiredArgsConstructor
class UserESRepoCustomImpl implements UserESRepoCustom {

    private final ElasticsearchOperations operations;

    @Override
    public List<UserES> searchUsers(final Page page, final SearchUsersQuery query) {

        // construir query dinâmica (parecida à CriteriaBuilder, mas para Elasticsearch)
        NativeQueryBuilder builder = NativeQuery.builder();

        if (StringUtils.hasText(query.getUsername())) {
            builder.withQuery(q -> q
                    .term(t -> t.field("username.keyword").value(query.getUsername())));
        }

        if (StringUtils.hasText(query.getFullName())) {
            builder.withQuery(q -> q
                    .match(m -> m.field("fullName").query(query.getFullName())));
        }

        builder.withPageable(org.springframework.data.domain.PageRequest.of(page.getNumber() - 1, page.getLimit()));

        Query builtQuery = builder.build();
        SearchHits<UserES> hits = operations.search(builtQuery, UserES.class);

        return hits.getSearchHits()
                .stream()
                .map(hit -> hit.getContent())
                .toList();
    }
}

