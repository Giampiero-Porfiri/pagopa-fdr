package it.gov.pagopa.fdr.rest.psps.validation;

import static io.opentelemetry.api.trace.SpanKind.SERVER;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import it.gov.pagopa.fdr.exception.AppErrorCodeMessageEnum;
import it.gov.pagopa.fdr.exception.AppException;
import it.gov.pagopa.fdr.rest.psps.request.AddPaymentRequest;
import it.gov.pagopa.fdr.rest.psps.request.CreateFlowRequest;
import it.gov.pagopa.fdr.rest.psps.request.DeletePaymentRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PspsValidationService {

  @Inject Logger log;

  @WithSpan(kind = SERVER)
  public void validateCreateFlow(String psp, CreateFlowRequest createFlowRequest) {
    log.debug("Validate create");
    if (!psp.equals(createFlowRequest.getSender().getPspId())) {
      throw new AppException(
          AppErrorCodeMessageEnum.REPORTING_FLOW_PSP_ID_NOT_MATCH,
          createFlowRequest.getReportingFlowName(),
          createFlowRequest.getSender().getPspId(),
          psp);
    }
  }

  @WithSpan(kind = SERVER)
  public void validateAddPayment(String psp, String fdr, AddPaymentRequest addPaymentRequest) {
    log.debug("Validate add payment");
  }

  @WithSpan(kind = SERVER)
  public void validateDeletePayment(
      String psp, String fdr, DeletePaymentRequest deletePaymentRequest) {
    log.debug("Validate delete payment");
  }

  @WithSpan(kind = SERVER)
  public void validatePublish(String psp, String fdr) {
    log.debug("Validate publish");
  }

  @WithSpan(kind = SERVER)
  public void validateDelete(String psp, String fdr) {
    log.debug("Validate delete");
  }
}
