package pt.psoft.g1.psoftg1.readermanagement.model.elasticsearch;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.readermanagement.services.UpdateReaderRequest;
import pt.psoft.g1.psoftg1.shared.model.elasticsearch.EntityWithPhotoES;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.util.List;

@Document(indexName = "reader_details")
public class ReaderDetailsES extends EntityWithPhotoES {

    @Id
    private String id;

    @Getter @Setter
    @Field(type = FieldType.Object)
    private Reader reader;

    @Getter @Field(type = FieldType.Object)
    private ReaderNumberES readerNumber;

    @Field(type = FieldType.Object)
    private BirthDateES birthDate;

    @Field(type = FieldType.Object)
    private PhoneNumberES phoneNumber;

    @Getter @Setter
    @Field(type = FieldType.Boolean)
    private boolean gdprConsent;

    @Getter @Setter
    @Field(type = FieldType.Boolean)
    private boolean marketingConsent;

    @Getter @Setter
    @Field(type = FieldType.Boolean)
    private boolean thirdPartySharingConsent;

    @Getter @Setter
    @Field(type = FieldType.Nested)
    private List<Genre> interestList;

    protected ReaderDetailsES() {}

    public ReaderDetailsES(int readerNumber, Reader reader, String birthDate, String phoneNumber,
                           boolean gdpr, boolean marketing, boolean thirdParty,
                           String photoURI, List<Genre> interestList) {

        if(reader == null || phoneNumber == null) {
            throw new IllegalArgumentException("Provided argument resolves to null object");
        }

        if(!gdpr) {
            throw new IllegalArgumentException("Readers must agree with the GDPR rules");
        }

        this.reader = reader;
        this.readerNumber = new ReaderNumberES(readerNumber);
        this.phoneNumber = new PhoneNumberES(phoneNumber);
        this.birthDate = new BirthDateES(birthDate);
        this.gdprConsent = true;
        this.marketingConsent = marketing;
        this.thirdPartySharingConsent = thirdParty;
        this.setPhotoInternal(photoURI);
        this.interestList = interestList;
    }

    public void applyPatch(UpdateReaderRequest request, String photoURI, List<Genre> interestList) {
        if(request.getUsername() != null) reader.setUsername(request.getUsername());
        if(request.getPassword() != null) reader.setPassword(request.getPassword());
        if(request.getFullName() != null) reader.setName(request.getFullName());
        if(request.getBirthDate() != null) birthDate = new BirthDateES(request.getBirthDate());
        if(request.getPhoneNumber() != null) phoneNumber = new PhoneNumberES(request.getPhoneNumber());
        marketingConsent = request.getMarketing();
        thirdPartySharingConsent = request.getThirdParty();
        if(photoURI != null) setPhotoInternal(photoURI);
        if(interestList != null) this.interestList = interestList;
    }

    public String getReaderNumber() {
        return readerNumber.toString();
    }

    public String getPhoneNumber() {
        return phoneNumber.toString();
    }
}
