package it.gov.pagopa.fdr.rest.info.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Info {
  private String name;
  private String version;
  private String environment;

  private String description;

  private List<ErrorCode> errorCodes;
}
