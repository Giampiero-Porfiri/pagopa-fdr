package it.gov.pagopa.fdr.repository.giampiero;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.annotation.security.DeclareRoles;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDate;
@Data
@MongoEntity(collection = "my_user")
public class User extends PanacheMongoEntity {

    private String nome;

    private String cognome;

    private LocalDate nascita;

    private long eta;
}
