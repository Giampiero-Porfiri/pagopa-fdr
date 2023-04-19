package it.gov.pagopa.fdr.service.reportingFlow;

import static io.opentelemetry.api.trace.SpanKind.SERVER;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import it.gov.pagopa.fdr.exception.AppErrorCodeMessageEnum;
import it.gov.pagopa.fdr.exception.AppException;
import it.gov.pagopa.fdr.repository.reportingFlow.ReportingFlow;
import it.gov.pagopa.fdr.repository.reportingFlow.ReportingFlowStatusEnum;
import it.gov.pagopa.fdr.repository.reportingFlow.projection.ReportingFlowId;
import it.gov.pagopa.fdr.repository.reportingFlow.projection.ReportingFlowNoPayment;
import it.gov.pagopa.fdr.repository.reportingFlow.projection.ReportingFlowOnlyPayment;
import it.gov.pagopa.fdr.service.reportingFlow.dto.AddPaymentDto;
import it.gov.pagopa.fdr.service.reportingFlow.dto.MetadataDto;
import it.gov.pagopa.fdr.service.reportingFlow.dto.ReportingFlowByIdEcDto;
import it.gov.pagopa.fdr.service.reportingFlow.dto.ReportingFlowDto;
import it.gov.pagopa.fdr.service.reportingFlow.dto.ReportingFlowGetDto;
import it.gov.pagopa.fdr.service.reportingFlow.dto.ReportingFlowGetPaymentDto;
import it.gov.pagopa.fdr.service.reportingFlow.mapper.ReportingFlowServiceMapper;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.bson.types.ObjectId;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ReportingFlowService {

  @Inject ReportingFlowServiceMapper mapper;

  @Inject Logger log;

  @WithSpan(kind = SERVER)
  public String save(ReportingFlowDto reportingFlowDto) {
    log.debugf("Save data on DB");

    Instant now = Instant.now();

    ReportingFlow reportingFlow = mapper.toReportingFlow(reportingFlowDto);
    reportingFlow.created = now;
    reportingFlow.updated = now;
    reportingFlow.status = ReportingFlowStatusEnum.NEW_LOAD;

    reportingFlow.persist();
    return reportingFlow.id.toString();
  }

  @WithSpan(kind = SERVER)
  public void addPayment(String id, AddPaymentDto addPaymentDto) {
    log.debugf("Save add payment on DB");

    Instant now = Instant.now();

    if (!ObjectId.isValid(id)) {
      throw new AppException(AppErrorCodeMessageEnum.REPORTING_FLOW_ID_INVALID, id);
    }
    ObjectId reportingFlowId = new ObjectId(id);

    Optional<ReportingFlow> byIdOptional = ReportingFlow.findByIdOptional(reportingFlowId);
    ReportingFlow flow =
        byIdOptional.orElseThrow(
            () -> new AppException(AppErrorCodeMessageEnum.REPORTING_FLOW_NOT_FOUND, id));

    flow.payments.addAll(mapper.toPagamentos(addPaymentDto.getPayments()));
    flow.updated = now;
    flow.status = ReportingFlowStatusEnum.ADD_PAYMENT;

    flow.update();
  }

  @WithSpan(kind = SERVER)
  public ReportingFlowGetDto findById(String id) {
    log.debugf("Get data from DB");

    if (!ObjectId.isValid(id)) {
      throw new AppException(AppErrorCodeMessageEnum.REPORTING_FLOW_ID_INVALID, id);
    }
    ObjectId reportingFlowId = new ObjectId(id);

    Optional<ReportingFlowNoPayment> reportingFlow =
        ReportingFlow.find("_id", reportingFlowId)
            .project(ReportingFlowNoPayment.class)
            .firstResultOptional();

    return reportingFlow
        .map(rf -> mapper.toReportingFlowGetDto(rf))
        .orElseThrow(() -> new AppException(AppErrorCodeMessageEnum.REPORTING_FLOW_NOT_FOUND, id));
  }

  @WithSpan(kind = SERVER)
  public ReportingFlowGetPaymentDto findPaymentById(String id) {
    log.debugf("Get data from DB");

    if (!ObjectId.isValid(id)) {
      throw new AppException(AppErrorCodeMessageEnum.REPORTING_FLOW_ID_INVALID, id);
    }
    ObjectId reportingFlowId = new ObjectId(id);

    Optional<ReportingFlowOnlyPayment> reportingFlow =
        ReportingFlow.find("_id", reportingFlowId)
            .project(ReportingFlowOnlyPayment.class)
            .firstResultOptional();

    return reportingFlow
        .map(rf -> mapper.toReportingFlowGetPaymentDto(rf))
        .orElseThrow(() -> new AppException(AppErrorCodeMessageEnum.REPORTING_FLOW_NOT_FOUND, id));
  }

  private Sort getSort(List<String> sortColumn) {
    Sort sort = Sort.empty();
    if (sortColumn != null && sortColumn.size() > 0) {
      sortColumn.stream()
          .filter(s -> s.replace(",", "").isBlank())
          .forEach(
              a -> {
                String[] split = a.split(",");
                String column = split[0].trim();
                String direction = split[1].trim();
                if (!column.isBlank()) {
                  if (direction.equalsIgnoreCase("asc")) {
                    sort.and(column, Direction.Ascending);
                  } else if (direction.equalsIgnoreCase("desc")) {
                    sort.and(column, Direction.Descending);
                  } else {
                    sort.and(column);
                  }
                }
              });
    }
    return sort;
  }

  @WithSpan(kind = SERVER)
  public ReportingFlowByIdEcDto findByIdEc(
      String idEc, String idPsp, int pageNumber, int pageSize, List<String> sortColumn) {
    log.debugf("Get all data from DB");

    Page page = Page.of(pageNumber - 1, pageSize);
    Sort sort = getSort(sortColumn);

    PanacheQuery<ReportingFlow> reportingFlowPanacheQuery = null;
    if (idPsp == null || idPsp.isBlank()) {
      reportingFlowPanacheQuery = ReportingFlow.find("receiver.idEc", sort, idEc);
    } else {
      reportingFlowPanacheQuery =
          ReportingFlow.find("receiver.idEc = ?1 and sender.idPsp = ?2", sort, idEc, idPsp);
    }
    PanacheQuery<ReportingFlowId> reportingFlowIdPanacheQuery =
        reportingFlowPanacheQuery.page(page).project(ReportingFlowId.class);
    List<ReportingFlowId> reportingFlowIds = reportingFlowIdPanacheQuery.list();

    int totPage = reportingFlowIdPanacheQuery.pageCount();
    long countReportingFlow = reportingFlowIdPanacheQuery.count();

    return ReportingFlowByIdEcDto.builder()
        .metadata(
            MetadataDto.builder()
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .totPage(totPage)
                .sortColumn(sortColumn)
                .build())
        .count(countReportingFlow)
        .data(reportingFlowIds.stream().map(rf -> rf.id.toString()).toList())
        .build();
  }

  @WithSpan(kind = SERVER)
  public void confirm(String id) {
    log.debugf("Update status id: [%s]", id);

    Optional<ReportingFlow> byIdOptional = ReportingFlow.findByIdOptional(id);
    ReportingFlow flow =
        byIdOptional.orElseThrow(
            () -> new AppException(AppErrorCodeMessageEnum.REPORTING_FLOW_NOT_FOUND, id));

    flow.status = ReportingFlowStatusEnum.CONFIRM;

    flow.persist();
  }
}
