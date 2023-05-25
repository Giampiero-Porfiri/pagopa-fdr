package it.gov.pagopa.fdr.service.dto.giampieroDto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UserDto {

    private String nome;

    private String cognome;

    private LocalDate nascita;
}
