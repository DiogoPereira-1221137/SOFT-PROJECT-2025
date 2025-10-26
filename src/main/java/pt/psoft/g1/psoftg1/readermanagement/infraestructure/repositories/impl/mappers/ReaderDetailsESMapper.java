package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.readermanagement.model.BirthDate;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.model.elasticsearch.*;
import pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl.mappers.PhotoMapperES;
import pt.psoft.g1.psoftg1.shared.model.Name;
import pt.psoft.g1.psoftg1.shared.model.elasticsearch.NameES;

@Mapper(componentModel = "spring", uses = { PhotoMapperES.class })
public interface ReaderDetailsESMapper {

    @Mapping(target = "photo", source = "photo") // para ES
    ReaderDetailsES toEntity(ReaderDetails model);

    @Mapping(target = "photo", source = "photo") // para JPA
    ReaderDetails toModel(ReaderDetailsES entity);

    default String map(ReaderNumberES readerNumberES) {
        if (readerNumberES == null) return null;
        return readerNumberES.toString();
    }

    default BirthDateES map(java.time.LocalDate birthDate) {
        if (birthDate == null) return null;
        return new BirthDateES(birthDate.getYear(), birthDate.getMonthValue(), birthDate.getDayOfMonth());
    }

    default BirthDateES map(BirthDate birthDate) {
        if (birthDate == null) return null;
        return new BirthDateES(birthDate.toString());
    }

    default String mapBirthDateToString(BirthDate birthDate) {
        if (birthDate == null) return null;
        return birthDate.toString();
    }

    default BirthDate mapStringToBirthDate(String birthDate) {
        if (birthDate == null) return null;
        return new BirthDate(birthDate);
    }


    default String map(PhoneNumberES phoneNumberES) {
        if (phoneNumberES == null) return null;
        return phoneNumberES.toString();
    }


    default String map(EmailAddressES emailAddressES) {
        if (emailAddressES == null) return null;
        return emailAddressES.getAddress();
    }

    default String map(Name name) {
        return name == null ? null : name.toString();
    }

    default String map(NameES nameES) {
        return nameES == null ? null : nameES.toString();
    }
}
