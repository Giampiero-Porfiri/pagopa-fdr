package it.gov.pagopa.fdr.service.reportingFlow.dto;

import java.util.List;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class DeletePaymentDto {

  private List<Long> indexPayments;
}
