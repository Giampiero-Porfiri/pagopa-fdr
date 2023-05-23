package it.gov.pagopa.fdr.rest.model;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Getter
@SuperBuilder
public class GenericResponse {

  @Schema(example = "Success")
  private String message;
}
