package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.readermanagement.model.PhoneNumber;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderNumber;
import pt.psoft.g1.psoftg1.readermanagement.model.elasticsearch.*;
import pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl.mappers.CommonESMapper;
import pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl.mappers.PhotoMapperES;

@Mapper(componentModel = "spring", uses = { PhotoMapperES.class, CommonESMapper.class }, imports = { ReaderDetailsESMapperHelper.class })
public interface ReaderDetailsESMapper {

    @Mapping(target = "photo", source = "photo")
    @Mapping(target = "readerNumber", expression = "java(ReaderDetailsESMapperHelper.toReaderNumberES(model))")
    @Mapping(target = "phoneNumber", expression = "java(ReaderDetailsESMapperHelper.toPhoneNumberES(model))")
    ReaderDetailsES toEntity(ReaderDetails model);

    @Mapping(target = "photo", source = "photo")
    ReaderDetails toModel(ReaderDetailsES entity);

    default ReaderNumberES map(ReaderNumber readerNumber) {
        if (readerNumber == null) return null;
        String numberStr = readerNumber.toString();
        String[] parts = numberStr.split("/");
        int year = Integer.parseInt(parts[0].trim());
        int seq = Integer.parseInt(parts[1].trim());
        return new ReaderNumberES(year, seq);
    }

    default ReaderNumber map(ReaderNumberES readerNumberES) {
        if (readerNumberES == null) return null;
        String numberStr = readerNumberES.getReaderNumber();
        String[] parts = numberStr.split("/");
        int year = Integer.parseInt(parts[0].trim());
        int seq = Integer.parseInt(parts[1].trim());
        return new ReaderNumber(year, seq);
    }

    default PhoneNumberES map(PhoneNumber phoneNumber) {
        if (phoneNumber == null) return null;
        return new PhoneNumberES(phoneNumber.toString());
    }

    default PhoneNumber map(PhoneNumberES phoneNumberES) {
        if (phoneNumberES == null) return null;
        return new PhoneNumber(phoneNumberES.toString());
    }

    default String map(EmailAddressES emailAddressES) {
        return emailAddressES == null ? null : emailAddressES.getAddress();
    }
}