package it.gov.pagopa.fdr.repository.giampiero;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.annotation.security.DeclareRoles;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.hibernate.validator.constraints.UniqueElements;

@MongoEntity(collection = "my_user")
public class User extends PanacheMongoEntity {

    public String nome;
    public String cognome;
    public Long eta;
}
