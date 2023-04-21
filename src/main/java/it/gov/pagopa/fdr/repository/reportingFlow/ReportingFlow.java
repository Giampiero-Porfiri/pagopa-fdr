package it.gov.pagopa.fdr.repository.reportingFlow;

import io.quarkus.mongodb.panache.common.MongoEntity;
import java.time.Instant;
import java.util.List;

@MongoEntity(collection = "reporting_flow")
public class ReportingFlow extends AbstractReportingFlow {

  public Long revision;
  public Instant created;
  public Instant updated;

  public String reportingFlow;
  public Instant dateReportingFlow;

  public Sender sender;
  public Receiver receiver;

  public String regulation;
  public Instant dateRegulation;

  public String bicCodePouringBank;

  public List<Pagamento> payments;

  public ReportingFlowStatusEnum status;
}
