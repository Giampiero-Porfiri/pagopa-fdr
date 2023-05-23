package it.gov.pagopa.fdr.service.giampiero;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.panache.common.Parameters;
import it.gov.pagopa.fdr.exception.AppErrorCodeMessageEnum;
import it.gov.pagopa.fdr.exception.AppException;


import it.gov.pagopa.fdr.repository.giampiero.User;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class GiampieroService {

    public void save(User utente) {
        utente.persist();
    }

    public void update(User utente, String nome) {
        Optional<User> userOpt = findUser(nome);

        if(!userOpt.isPresent()) {
            throw new AppException(AppErrorCodeMessageEnum.BAD_REQUEST);
        }

        User user = userOpt.get();

        user.nome = utente.nome;
        user.cognome = utente.cognome;
        user.eta = utente.eta;

    }

    public void delete(String nome) {
        Long res = User.delete("nome = :nome", Parameters.with("nome", nome));

        if(res == 0) {
            throw new AppException(AppErrorCodeMessageEnum.BAD_REQUEST);
        }
    }

    public Optional<User> findUser(String nome) {
        return User.find("nome = :nome", Parameters.with("nome", nome)).firstResultOptional();
    }

}
