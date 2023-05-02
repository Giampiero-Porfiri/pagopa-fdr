package it.gov.pagopa.fdr.repository.reportingFlow;

import io.quarkus.mongodb.panache.common.MongoEntity;
import it.gov.pagopa.fdr.repository.reportingFlow.model.AbstractReportingFlowEntity;

@MongoEntity(collection = "fdr_publish")
public class FdrPublishEntity extends AbstractReportingFlowEntity {}
