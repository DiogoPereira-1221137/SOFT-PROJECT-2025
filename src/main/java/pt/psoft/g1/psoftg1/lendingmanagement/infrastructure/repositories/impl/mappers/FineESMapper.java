package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl.mappers;

import org.mapstruct.Mapper;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.model.elasticSearch.FineES;
import pt.psoft.g1.psoftg1.lendingmanagement.model.elasticSearch.LendingNumberES;
import pt.psoft.g1.psoftg1.readermanagement.model.BirthDate;
import pt.psoft.g1.psoftg1.shared.model.Name;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.shared.model.elasticsearch.NameES;
import pt.psoft.g1.psoftg1.shared.model.elasticsearch.PhotoES;


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


    default String map(PhotoES photoES) {
        return photoES == null ? null : photoES.toString();
    }

    default String map(Photo photo) {
        return photo == null ? null : photo.toString();
    }

    default String map(NameES nameES) {
        return nameES == null ? null : nameES.toString();
    }

    default String map(Name name) {
        return name == null ? null : name.toString();
    }

    default String map(BirthDate birthDate) {
        return birthDate == null ? null : birthDate.toString();
    }
}

