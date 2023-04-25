package it.gov.pagopa.fdr.exception;

import it.gov.pagopa.fdr.util.AppConstant;
import it.gov.pagopa.fdr.util.AppMessageUtil;
import org.jboss.resteasy.reactive.RestResponse;

public enum AppErrorCodeMessageEnum implements AppErrorCodeMessageInterface {
  ERROR("0500", "system.error", RestResponse.Status.INTERNAL_SERVER_ERROR),
  BAD_REQUEST("0400", "bad.request", RestResponse.Status.BAD_REQUEST),
  BAD_REQUEST_INPUT_JSON("0401", "bad.request.inputJson", RestResponse.Status.BAD_REQUEST),
  BAD_REQUEST_INPUT_JSON_INSTANT(
      "0402", "bad.request.inputJson.instant", RestResponse.Status.BAD_REQUEST),
  BAD_REQUEST_INPUT_JSON_ENUM(
      "0403", "bad.request.inputJson.enum", RestResponse.Status.BAD_REQUEST),

  BAD_REQUEST_INPUT_JSON_DESERIALIZE_ERROR(
      "0404", "bad.request.inputJson.deserialize", RestResponse.Status.BAD_REQUEST),
  BAD_REQUEST_INPUT_JSON_NON_VALID_FORMAT(
      "0405", "bad.request.inputJson.notValidJsonFormat", RestResponse.Status.BAD_REQUEST),

  REPORTING_FLOW_NOT_FOUND("0701", "reporting-flow.notFound", RestResponse.Status.NOT_FOUND),
  REPORTING_FLOW_ALREADY_EXIST(
      "0702", "reporting-flow.alreadyExist", RestResponse.Status.BAD_REQUEST),
  REPORTING_FLOW_WRONG_ACTION("0703", "reporting-flow.wrogAction", RestResponse.Status.BAD_REQUEST),
  REPORTING_FLOW_NO_PAYMENT("0704", "reporting-flow.noPayment", RestResponse.Status.BAD_REQUEST),
  REPORTING_FLOW_PAYMENT_SAME_INDEX_IN_SAME_REQUEST(
      "0705", "reporting-flow.sameIndexInSameRequest", RestResponse.Status.BAD_REQUEST),
  REPORTING_FLOW_PAYMENT_DUPLICATE_INDEX(
      "0706", "reporting-flow.duplicateIndex", RestResponse.Status.BAD_REQUEST);
  private final String errorCode;
  private final String errorMessageKey;
  private final RestResponse.Status httpStatus;

  AppErrorCodeMessageEnum(
      String errorCode, String errorMessageKey, RestResponse.Status httpStatus) {
    this.errorCode = errorCode;
    this.errorMessageKey = errorMessageKey;
    this.httpStatus = httpStatus;
  }

  @Override
  public String errorCode() {
    return AppConstant.SERVICE_CODE_APP + "-" + errorCode;
  }

  @Override
  public String message(Object... args) {
    return AppMessageUtil.getMessage(errorMessageKey, args);
  }

  @Override
  public RestResponse.Status httpStatus() {
    return httpStatus;
  }
}
