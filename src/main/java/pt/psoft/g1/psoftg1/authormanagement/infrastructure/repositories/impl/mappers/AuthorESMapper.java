package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.elasticsearch.AuthorES;
import pt.psoft.g1.psoftg1.authormanagement.model.elasticsearch.BioES;
import pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl.mappers.PhotoMapperES;
import pt.psoft.g1.psoftg1.shared.model.elasticsearch.NameES;

@Mapper(componentModel = "spring", uses = { PhotoMapperES.class })
public interface AuthorESMapper {

    // Constrói explicitamente NameES e BioES para evitar ambiguidade do MapStruct
    @Mapping(target = "name", expression = "java(new pt.psoft.g1.psoftg1.shared.model.elasticsearch.NameES(author.getName()))")
    @Mapping(target = "bio", expression = "java(new pt.psoft.g1.psoftg1.authormanagement.model.elasticsearch.BioES(author.getBio()))")
    AuthorES toEntity(Author author);

    // Para o mapeamento inverso, constrói Author usando os getters string (o domínio não pode mudar)
    @Mapping(target = "name", expression = "java(new pt.psoft.g1.psoftg1.shared.model.Name(authorES.getName() != null ? authorES.getName().getName() : null))")
    @Mapping(target = "bio", expression = "java(authorES.getBio() != null ? new pt.psoft.g1.psoftg1.authormanagement.model.Bio(authorES.getBio().getBio()) : null)")
    Author toModel(AuthorES authorES);

    // NOTA: mantive o mapper minimal — não é necessário helpers adicionais aqui.
}
