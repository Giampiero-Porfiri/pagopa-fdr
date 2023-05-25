package it.gov.pagopa.fdr.rest.giampiero.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;

@Data
@Builder
@Jacksonized // da http (stringa json) in un json e infine bean, costruttore che in automatico fa wrap
public class User {
    @NotNull
    @Schema(example = "Giovanni")
    String nome;
    @Schema(example = "Ciccio")
    String cognome;
    @Schema(example = "1990-05-15")
    LocalDate nascita;
}
