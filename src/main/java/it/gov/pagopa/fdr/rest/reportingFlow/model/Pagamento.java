package it.gov.pagopa.fdr.rest.reportingFlow.model;

import java.math.BigDecimal;
import java.time.Instant;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Getter
@Builder
@Jacksonized
public class Pagamento {

  @NotNull
  @Pattern(regexp = "^\\w+$")
  @Schema(example = "abcdefg")
  private String identificativoUnivocoVersamento;

  @NotNull
  @Pattern(regexp = "^\\w+$")
  @Schema(example = "abcdefg")
  private String identificativoUnivocoRiscossione;

  @NotNull
  @Schema(example = "1")
  private Long indiceDatiSingoloPagamento;

  @NotNull
  @DecimalMin(value = "0.0", inclusive = false)
  @Digits(integer = Integer.MAX_VALUE, fraction = 2)
  @Schema(example = "0.01")
  private BigDecimal singoloImportoPagato;

  @NotNull
  @Schema(example = "PAGAMENTO_ESEGUITO")
  private CodiceEsitoPagamentoEnum codiceEsitoSingoloPagamento;

  @NotNull
  @Schema(example = "2023-02-03T12:00:30.900000Z")
  private Instant dataEsitoSingoloPagamento;
}
