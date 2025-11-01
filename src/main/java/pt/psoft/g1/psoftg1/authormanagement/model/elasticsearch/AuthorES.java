package pt.psoft.g1.psoftg1.authormanagement.model.elasticsearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import pt.psoft.g1.psoftg1.authormanagement.model.Bio;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.shared.model.elasticsearch.EntityWithPhotoES;
import pt.psoft.g1.psoftg1.shared.model.elasticsearch.NameES;

@Document(indexName = "authors")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorES extends EntityWithPhotoES {

    @Id
    private Long authorNumber; // ID em formato String (UUID ou Long convertido)

    @Version
    private Long version;

    @JsonProperty("name")
    @Field(type = FieldType.Object)
    private NameES name;

    @JsonProperty("bio")
    @Field(type = FieldType.Object)
    private BioES bio;



    // --- MÉTODOS DE NEGÓCIO ---
    public void applyPatch(final long desiredVersion, final UpdateAuthorRequest request) {
        if (this.version != null && !this.version.equals(desiredVersion)) {
            throw new ConflictException("Object was already modified by another user");
        }

        if (request.getName() != null)
            this.name = new NameES(request.getName());

        if (request.getBio() != null)
            this.bio = new BioES(request.getBio());

        if (request.getPhotoURI() != null)
            setPhotoInternal(request.getPhotoURI());
    }

    public void removePhoto(long desiredVersion) {
        if (this.version != null && !this.version.equals(desiredVersion)) {
            throw new ConflictException("Provided version does not match latest version of this object");
        }
        setPhotoInternal(null);
    }

    public String getName() {
        return name != null ? name.toString() : null;
    }

    public String getBio() {
        return bio != null ? bio.toString() : null;

    }

//    public Long getIdAsLong() {
//        try {
//            return authorNumber != null ? Long.parseLong(authorNumber) : null;
//        } catch (NumberFormatException e) {
//            return null; // Pode acontecer se for UUID
//        }
//    }
}
