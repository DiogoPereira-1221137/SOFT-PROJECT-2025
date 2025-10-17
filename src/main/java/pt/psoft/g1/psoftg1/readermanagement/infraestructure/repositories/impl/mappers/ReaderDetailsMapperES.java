package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl.mappers;

import org.mapstruct.Mapper;
import pt.psoft.g1.psoftg1.lendingmanagement.model.elasticSearch.LendingNumberES;
import pt.psoft.g1.psoftg1.readermanagement.model.BirthDate;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.model.elasticsearch.*;
import pt.psoft.g1.psoftg1.shared.model.Photo;

@Mapper(componentModel = "spring")
public interface ReaderDetailsMapperES {

    ReaderDetails toModel(ReaderDetailsES entity);
    ReaderDetailsES toEntity(ReaderDetails model);

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

    default String mapPhotoToString(Photo photo) {
        if (photo == null) return null;
        return photo.toString();
    }

    default String map(EmailAddressES emailAddressES) {
        if (emailAddressES == null) return null;
        return emailAddressES.getAddress();
    }
}
