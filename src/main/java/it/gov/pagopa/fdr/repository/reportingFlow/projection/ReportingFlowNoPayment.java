package it.gov.pagopa.fdr.repository.reportingFlow.projection;

import io.quarkus.mongodb.panache.common.ProjectionFor;
import it.gov.pagopa.fdr.repository.reportingFlow.collection.ReportingFlow;
import it.gov.pagopa.fdr.repository.reportingFlow.collection.model.Receiver;
import it.gov.pagopa.fdr.repository.reportingFlow.collection.model.ReportingFlowStatusEnum;
import it.gov.pagopa.fdr.repository.reportingFlow.collection.model.Sender;
import java.time.Instant;
import org.bson.types.ObjectId;

@ProjectionFor(ReportingFlow.class)
public class ReportingFlowNoPayment {

  public ObjectId id;

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

  public ReportingFlowStatusEnum status;
}
