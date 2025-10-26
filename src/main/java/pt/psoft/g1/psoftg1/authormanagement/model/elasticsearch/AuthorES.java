package pt.psoft.g1.psoftg1.authormanagement.model.elasticsearch;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.shared.model.elasticsearch.EntityWithPhotoES;
import pt.psoft.g1.psoftg1.shared.model.elasticsearch.NameES;

@Document(indexName = "authors") // Define o índice Elasticsearch
@NoArgsConstructor
@Getter
@Setter
public class AuthorES extends EntityWithPhotoES {

    @Id
    private String authorNumber; // Em Elasticsearch, o ID normalmente é String

    @Version
    private Long version; // Suporte a versionamento otimista

    @Field(type = FieldType.Object)
    private NameES name;

    @Field(type = FieldType.Text)
    private String bio;

    public AuthorES(String name, String bio, String photoURI) {
        setName(new NameES(name));
        setBio(bio);
        setPhotoInternal(photoURI);
    }

    public void applyPatch(final long desiredVersion, final UpdateAuthorRequest request) {
        if (!this.version.equals(desiredVersion)) {
            throw new ConflictException("Object was already modified by another user");
        }
        if (request.getName() != null) setName(new NameES(request.getName()));
        if (request.getBio() != null) setBio(request.getBio());
        if (request.getPhotoURI() != null) setPhotoInternal(request.getPhotoURI());
    }

    public void removePhoto(long desiredVersion) {
        if (!this.version.equals(desiredVersion)) {
            throw new ConflictException("Provided version does not match latest version of this object");
        }
        setPhotoInternal(null);
    }

    public String getNameAsString() {
        return this.name.toString();
    }
}

