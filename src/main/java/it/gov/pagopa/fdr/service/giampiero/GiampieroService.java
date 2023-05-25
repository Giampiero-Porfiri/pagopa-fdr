package it.gov.pagopa.fdr.service.giampiero;

import io.quarkus.panache.common.Parameters;
import it.gov.pagopa.fdr.exception.AppErrorCodeMessageEnum;
import it.gov.pagopa.fdr.exception.AppException;
import it.gov.pagopa.fdr.repository.giampiero.User;
import it.gov.pagopa.fdr.service.dto.giampieroDto.UserDto;
import it.gov.pagopa.fdr.service.psps.mapper.giampieroMapper.UserMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@ApplicationScoped
public class GiampieroService {

    @Inject UserMapper mapper;

    @Inject Logger log;

    public void save(UserDto utente) {
        User user = mapper.toUser(utente);
        //mapper.toEta(utente);
        user.persist();
    }

    public void update(UserDto utente, String nome) {
        // log.debugf("Update User on DB");

        // Recupera dal DB il modello del vecchio utente
        Optional<User> userOpt = User.find("nome = :nome", Parameters.with("nome", nome)).firstResultOptional();
        if(!userOpt.isPresent()) {
            throw new AppException(AppErrorCodeMessageEnum.BAD_REQUEST);
        }
        User oldUser = userOpt.get();
        mapper.updateUser(utente,oldUser);
        /* oldUser.nome = user.nome;
        oldUser.cognome = user.cognome;
        oldUser.nascita = user.nascita;
        oldUser.setNome(utente.getNome());
        oldUser.setCognome(utente.getCognome());
        oldUser.setNascita(utente.getNascita());
        oldUser.setEta(ChronoUnit.YEARS.between(utente.getNascita(),LocalDate.now()));*/
        oldUser.update();
    }

    public void delete(String nome) {
        Long res = User.delete("nome = :nome", Parameters.with("nome", nome));

        if(res == 0) {
            throw new AppException(AppErrorCodeMessageEnum.BAD_REQUEST);
        }
    }
}
