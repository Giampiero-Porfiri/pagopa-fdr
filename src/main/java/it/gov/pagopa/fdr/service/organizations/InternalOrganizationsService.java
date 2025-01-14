package it.gov.pagopa.fdr.service.organizations;

import static io.opentelemetry.api.trace.SpanKind.SERVER;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import it.gov.pagopa.fdr.exception.AppErrorCodeMessageEnum;
import it.gov.pagopa.fdr.exception.AppException;
import it.gov.pagopa.fdr.repository.fdr.FdrPaymentPublishEntity;
import it.gov.pagopa.fdr.repository.fdr.FdrPublishEntity;
import it.gov.pagopa.fdr.repository.fdr.projection.FdrPublishReportingFlowNameProjection;
import it.gov.pagopa.fdr.service.dto.FlowDto;
import it.gov.pagopa.fdr.service.dto.MetadataDto;
import it.gov.pagopa.fdr.service.dto.ReportingFlowByIdEcDto;
import it.gov.pagopa.fdr.service.dto.ReportingFlowGetDto;
import it.gov.pagopa.fdr.service.dto.ReportingFlowGetPaymentDto;
import it.gov.pagopa.fdr.service.organizations.mapper.InternalOrganizationsServiceServiceMapper;
import it.gov.pagopa.fdr.util.AppDBUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.Instant;
import java.util.List;
import org.jboss.logging.Logger;

@ApplicationScoped
public class InternalOrganizationsService {

  @Inject InternalOrganizationsServiceServiceMapper mapper;

  @Inject Logger log;

  private static final String PSP_ID = "pspId";
  private static final String FLOW_NAME = "flowName";

  @WithSpan(kind = SERVER)
  public ReportingFlowByIdEcDto findByInternals(long pageNumber, long pageSize) {
    log.debugf("Get all data from DB");

    Page page = Page.of((int) pageNumber - 1, (int) pageSize);
    Sort sort = AppDBUtil.getSort(List.of("_id,asc"));

    PanacheQuery<FdrPublishEntity> reportingFlowPanacheQuery =
        FdrPublishEntity.find(
            "receiver.internal_ndp_read = :internalRead",
            sort,
            Parameters.with("internalRead", Boolean.TRUE).map());

    PanacheQuery<FdrPublishReportingFlowNameProjection> reportingFlowNameProjectionPanacheQuery =
        reportingFlowPanacheQuery.page(page).project(FdrPublishReportingFlowNameProjection.class);

    List<FdrPublishReportingFlowNameProjection> reportingFlowIds =
        reportingFlowNameProjectionPanacheQuery.list();

    long totPage = reportingFlowNameProjectionPanacheQuery.pageCount();
    long countReportingFlow = reportingFlowNameProjectionPanacheQuery.count();

    return ReportingFlowByIdEcDto.builder()
        .metadata(
            MetadataDto.builder()
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .totPage(totPage)
                .build())
        .count(countReportingFlow)
        .data(
            reportingFlowIds.stream()
                .map(
                    rf ->
                        FlowDto.builder()
                            .name(rf.getReportingFlowName())
                            .pspId(rf.getSender().getPspId())
                            .build())
                .toList())
        .build();
  }

  @WithSpan(kind = SERVER)
  public ReportingFlowGetDto findByReportingFlowNameInternals(
      String reportingFlowName, String pspId) {
    log.debugf("Get data from DB");

    FdrPublishEntity reportingFlowEntity =
        FdrPublishEntity.find(
                "reporting_flow_name = :%s and sender.psp_id = :%s".formatted(FLOW_NAME, PSP_ID),
                Parameters.with(FLOW_NAME, reportingFlowName).and(PSP_ID, pspId).map())
            .project(FdrPublishEntity.class)
            .firstResultOptional()
            .orElseThrow(
                () ->
                    new AppException(
                        AppErrorCodeMessageEnum.REPORTING_FLOW_NOT_FOUND, reportingFlowName));

    return mapper.toReportingFlowGetDto(reportingFlowEntity);
  }

  @WithSpan(kind = SERVER)
  public ReportingFlowGetPaymentDto findPaymentByReportingFlowNameInternals(
      String reportingFlowName, String pspId, long pageNumber, long pageSize) {
    log.debugf("Get data from DB");

    Page page = Page.of((int) pageNumber - 1, (int) pageSize);
    Sort sort = AppDBUtil.getSort(List.of("index,asc"));

    PanacheQuery<FdrPaymentPublishEntity> reportingFlowPaymentEntityPanacheQuery =
        FdrPaymentPublishEntity.find(
                "ref_fdr_reporting_flow_name = :%s and sender.psp_id = :%s"
                    .formatted(FLOW_NAME, PSP_ID),
                sort,
                Parameters.with(FLOW_NAME, reportingFlowName).and(PSP_ID, pspId).map())
            .page(page);

    List<FdrPaymentPublishEntity> list = reportingFlowPaymentEntityPanacheQuery.list();

    long totPage = reportingFlowPaymentEntityPanacheQuery.pageCount();
    long countReportingFlowPayment = reportingFlowPaymentEntityPanacheQuery.count();

    return ReportingFlowGetPaymentDto.builder()
        .metadata(
            MetadataDto.builder()
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .totPage(totPage)
                .build())
        .count(countReportingFlowPayment)
        .data(mapper.toPagamentoDtos(list))
        .build();
  }

  @WithSpan(kind = SERVER)
  public void changeInternalReadFlag(String pspId, String reportingFlowName) {
    log.debugf("Change read flag");

    Instant now = Instant.now();
    FdrPublishEntity reportingFlowEntity =
        FdrPublishEntity.find(
                "reporting_flow_name = :%s and sender.psp_id = :%s".formatted(FLOW_NAME, PSP_ID),
                Parameters.with(FLOW_NAME, reportingFlowName).and(PSP_ID, pspId).map())
            .project(FdrPublishEntity.class)
            .firstResultOptional()
            .orElseThrow(
                () ->
                    new AppException(
                        AppErrorCodeMessageEnum.REPORTING_FLOW_NOT_FOUND, reportingFlowName));
    reportingFlowEntity.setUpdated(now);
    reportingFlowEntity.setInternalNdpRead(Boolean.TRUE);
    reportingFlowEntity.update();
  }
}
