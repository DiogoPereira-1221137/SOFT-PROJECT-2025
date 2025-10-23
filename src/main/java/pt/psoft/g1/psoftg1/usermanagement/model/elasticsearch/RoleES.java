package pt.psoft.g1.psoftg1.usermanagement.model.elasticsearch;

import java.io.Serial;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Elasticsearch version of Role
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleES implements GrantedAuthority {

    @Serial
    private static final long serialVersionUID = 1L;

    public static final String ADMIN = "ADMIN";
    public static final String LIBRARIAN = "LIBRARIAN";
    public static final String READER = "READER";

    @Field(type = FieldType.Keyword)
    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}
