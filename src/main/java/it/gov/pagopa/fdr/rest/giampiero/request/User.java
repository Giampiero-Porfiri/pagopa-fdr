package it.gov.pagopa.fdr.rest.giampiero.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Getter
@Builder
@Jacksonized // da http (stringa json) in un json e infine bean, costruttore che in automatico fa wrap
public class User {

    @NotNull
    @Schema(example = "Giovanni")
    String nome;
    @Schema(example = "Ciccio")
    String cognome;
    @Schema(example = "80")
    Long eta;


}
