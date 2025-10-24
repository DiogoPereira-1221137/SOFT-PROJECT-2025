package pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import pt.psoft.g1.psoftg1.shared.model.elasticsearch.ForbiddenNameES;

import java.util.List;
import java.util.Optional;

public interface SpringDataForbiddenNameESRepository extends ElasticsearchRepository<ForbiddenNameES, String> {

    // Busca nomes proibidos que estão contidos no padrão fornecido
    List<ForbiddenNameES> findByForbiddenNameContaining(String pat);

    // Busca exata por nome proibido
    Optional<ForbiddenNameES> findByForbiddenName(String forbiddenName);

    // Exclusão por nome proibido (não é suportado diretamente, precisa de lógica customizada)
}