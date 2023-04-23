package it.gov.pagopa.fdr.service.reportingFlow.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SenderDto {

  private SenderTypeDto type;

  private String id;

  private String pspId;

  private String pspName;

  private String brokerId;

  private String channelId;

  private String password;
}
