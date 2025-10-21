package pt.psoft.g1.psoftg1.shared.model.elasticsearch;

import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

@Getter
public abstract class EntityWithPhotoES {

    @Field(type = FieldType.Object)
    protected PhotoES photo;

    public void setPhoto(String photoUri) {
        this.setPhotoInternal(photoUri);
    }

    protected void setPhotoInternal(String photoURI) {
        if (photoURI == null) {
            this.photo = null;
        } else {
            try {
                this.photo = new PhotoES(Path.of(photoURI));
            } catch (InvalidPathException e) {
                this.photo = null;
            }
        }
    }
}