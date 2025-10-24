package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl.mappers;

import org.mapstruct.Mapper;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.model.elasticSearch.LendingES;
import pt.psoft.g1.psoftg1.lendingmanagement.model.elasticSearch.LendingNumberES;

@Mapper(componentModel = "spring")
public interface LendingESMapper {
    Lending toModel(LendingES entity);
    LendingES toEntity(Lending model);

    default LendingNumberES map(String lendingNumber) {
        if(lendingNumber == null) return null;
        return new LendingNumberES(lendingNumber);
    }

    default String map(LendingNumberES lendingNumberES) {
        if(lendingNumberES == null) return null;
        return lendingNumberES.toString();
    }
}

