package pt.psoft.g1.psoftg1.authormanagement.model.elasticsearch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import pt.psoft.g1.psoftg1.shared.model.StringUtilsCustom;

@NoArgsConstructor
@Getter
@Setter
public class BioES {

    private static final int BIO_MAX_LENGTH = 4096;

    @Field(type = FieldType.Text) // Campo indexado para busca full-text
    private String bio;

    public BioES(String bio) {
        setBio(bio);
    }

    public void setBio(String bio) {
        if (bio == null)
            throw new IllegalArgumentException("Bio cannot be null");
        if (bio.isBlank())
            throw new IllegalArgumentException("Bio cannot be blank");
        if (bio.length() > BIO_MAX_LENGTH)
            throw new IllegalArgumentException("Bio has a maximum of 4096 characters");

        this.bio = StringUtilsCustom.sanitizeHtml(bio);
    }

    @Override
    public String toString() {
        return bio;
    }
}
