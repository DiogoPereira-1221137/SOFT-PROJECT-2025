package pt.psoft.g1.psoftg1.shared.model.elasticsearch;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.nio.file.Path;


@Getter
@Setter
public class PhotoES {

    @Field(type = FieldType.Text)
    private String photoFile;

    public PhotoES() {}

    public PhotoES(Path photoPath) {
        this.photoFile = photoPath.toString();
    }
}
