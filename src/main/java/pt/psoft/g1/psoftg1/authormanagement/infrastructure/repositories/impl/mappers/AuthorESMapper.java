package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.mappers;

import org.mapstruct.Mapper;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.elasticsearch.AuthorES;
import pt.psoft.g1.psoftg1.authormanagement.model.elasticsearch.BioES;
import pt.psoft.g1.psoftg1.readermanagement.model.BirthDate;
import pt.psoft.g1.psoftg1.readermanagement.model.elasticsearch.BirthDateES;
import pt.psoft.g1.psoftg1.readermanagement.model.elasticsearch.EmailAddressES;
import pt.psoft.g1.psoftg1.readermanagement.model.elasticsearch.PhoneNumberES;
import pt.psoft.g1.psoftg1.readermanagement.model.elasticsearch.ReaderNumberES;
import pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl.mappers.PhotoMapperES;
import pt.psoft.g1.psoftg1.shared.model.Name;
import pt.psoft.g1.psoftg1.shared.model.elasticsearch.NameES;

@Mapper(componentModel = "spring", uses = { PhotoMapperES.class })
public interface  AuthorESMapper {

    AuthorES toEntity(Author model);

    Author toModel(AuthorES entity);

    default String map(PhoneNumberES phoneNumberES) {
        if (phoneNumberES == null) return null;
        return phoneNumberES.toString();
    }

    default String map(Name name) {
        return name == null ? null : name.toString();
    }

    default String map(NameES nameES) {
        return nameES == null ? null : nameES.toString();
    }

    default NameES map(String name) {
        return name == null ? null : new NameES(name);
    }
}
