package it.gov.pagopa.fdr.service.reportingFlow.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MetadataDto {

  private long pageSize;

  private long pageNumber;

  private long totPage;
}
