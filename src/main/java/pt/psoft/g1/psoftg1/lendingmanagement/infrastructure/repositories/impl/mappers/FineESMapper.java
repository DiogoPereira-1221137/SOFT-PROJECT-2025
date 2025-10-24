package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl.mappers;

import org.mapstruct.Mapper;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.model.elasticSearch.FineES;
import pt.psoft.g1.psoftg1.lendingmanagement.model.elasticSearch.LendingNumberES;

@Mapper(componentModel = "spring")
public interface FineESMapper {
    Fine toModel(FineES entity);
    FineES toEntity(Fine model);

    default LendingNumberES map(String lendingNumber) {
        if(lendingNumber == null) return null;
        return new LendingNumberES(lendingNumber);
    }

    default String map(LendingNumberES lendingNumberES) {
        if(lendingNumberES == null) return null;
        return lendingNumberES.toString();
    }
}

