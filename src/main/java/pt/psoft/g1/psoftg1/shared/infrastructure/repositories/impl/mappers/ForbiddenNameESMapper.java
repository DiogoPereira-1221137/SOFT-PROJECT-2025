package pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.model.elasticSearch.FineES;
import pt.psoft.g1.psoftg1.shared.model.ForbiddenName;
import pt.psoft.g1.psoftg1.shared.model.elasticsearch.ForbiddenNameES;

@Mapper
public interface ForbiddenNameESMapper {

    ForbiddenNameESMapper INSTANCE = Mappers.getMapper(ForbiddenNameESMapper.class);

    ForbiddenName toModel(ForbiddenNameES entity);
    ForbiddenNameES toEntity(ForbiddenName model);
}