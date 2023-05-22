package it.gov.pagopa.fdr.rest.giampiero;

import it.gov.pagopa.fdr.exception.AppErrorCodeMessageEnum;
import it.gov.pagopa.fdr.exception.AppException;
import it.gov.pagopa.fdr.rest.giampiero.request.User;
import it.gov.pagopa.fdr.rest.giampiero.response.UserResponse;
import it.gov.pagopa.fdr.rest.model.GenericResponse;
import it.gov.pagopa.fdr.service.giampiero.GiampieroService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.Optional;

@Path("/user")
public class GiampieroResource {
    @Inject
    Logger log;

    @Inject
    GiampieroService service;

    @Operation(
            summary = "Creates a new user",
            description = "Given a request user, this creates a new repository user")
    @RequestBody(content = @Content(schema = @Schema(implementation = User.class)))
    @APIResponses(
            value = {
                    @APIResponse(ref = "#/components/responses/InternalServerError"),
                    @APIResponse(ref = "#/components/responses/ValidationBadRequest"),
                    @APIResponse(ref = "#/components/responses/AppException400"),
                    @APIResponse(ref = "#/components/responses/AppException404"),
                    @APIResponse(
                            responseCode = "200",
                            description = "Success",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = UserResponse.class)
                            )
                    )
            }
    )
    @POST
    public RestResponse<UserResponse> createUser(@Valid User utente) {
        log.info("----------\n Sto creando utente:" + utente.getNome() + "\n ----------\n");

        // Non puoi essere Giampiero!
        if(utente.getNome().equals("Giampiero")) {
            log.info("----------\n Lanciata Giampiero Exception\n ----------\n");
            throw new AppException(AppErrorCodeMessageEnum.GIAMPIERO_ERRORE);
        }

        log.info("----------\n Convertendo model\n ----------\n");
        it.gov.pagopa.fdr.repository.giampiero.User user = new it.gov.pagopa.fdr.repository.giampiero.User();
        user.nome = utente.getNome();
        user.cognome = utente.getCognome();
        user.eta = utente.getEta();
        log.info("----------\n Salvando model\n ----------\n");
        service.save(user);
        return RestResponse.status(RestResponse.Status.CREATED,
                                    UserResponse.builder().messaggio("Benvenuto " + utente.getNome() + "!").build());
    }

    @Operation(
            summary = "Deletes an existing user",
            description = "Given a name, deletes the user")
    @RequestBody(content = @Content(schema = @Schema(implementation = User.class)))
    @APIResponses(
            value = {
                    @APIResponse(ref = "#/components/responses/InternalServerError"),
                    @APIResponse(ref = "#/components/responses/ValidationBadRequest"),
                    @APIResponse(ref = "#/components/responses/AppException400"),
                    @APIResponse(ref = "#/components/responses/AppException404"),
                    @APIResponse(
                            responseCode = "200",
                            description = "Success",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = GenericResponse.class)
                            )
                    )
            }
    )
    @DELETE
    @Path("/{name}")
    public RestResponse<UserResponse> deleteUserByNome(@PathParam("name") String nome) {
        log.info("----------\n Cancellando: " + nome + "----------\n");

        service.delete(nome);
        return RestResponse.status(RestResponse.Status.NO_CONTENT,
                                    UserResponse.builder().messaggio("Ho cancellato " + nome).build());
    }

    @Operation(
            summary = "Updates a user",
            description = "Given a name, updates the user")
    @RequestBody(content = @Content(schema = @Schema(implementation = User.class)))
    @APIResponses(
            value = {
                    @APIResponse(ref = "#/components/responses/InternalServerError"),
                    @APIResponse(ref = "#/components/responses/ValidationBadRequest"),
                    @APIResponse(ref = "#/components/responses/AppException400"),
                    @APIResponse(ref = "#/components/responses/AppException404"),
                    @APIResponse(
                            responseCode = "200",
                            description = "Success",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = GenericResponse.class)
                            )
                    )
            }
    )
    @PUT
    @Path("/{name}")
    public RestResponse<UserResponse> updateUser(@PathParam("name") String nome, User utente) {
        log.info("----------\n Modificando: " + nome + "\n----------\n");

        // Converto in user di repository da user di request
        log.info("----------\n Convertendo model\n ----------\n");
        it.gov.pagopa.fdr.repository.giampiero.User user = new it.gov.pagopa.fdr.repository.giampiero.User();
        user.nome = utente.getNome();
        user.cognome = utente.getCognome();
        user.eta = utente.getEta();

        log.info("----------\n Aggiornando model\n ----------\n");
        service.update(user, nome);
        return RestResponse.status(RestResponse.Status.OK,
                                    UserResponse.builder().messaggio("Ho modificato " + utente.getNome()).build());
    }
}
