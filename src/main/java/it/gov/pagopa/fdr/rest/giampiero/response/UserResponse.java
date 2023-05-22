package it.gov.pagopa.fdr.rest.giampiero.response;

import it.gov.pagopa.fdr.rest.model.GenericResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Getter
@SuperBuilder
@Jacksonized
public class UserResponse extends GenericResponse {

    @Override
    @Schema(example = "Success user operation ok")
    public String getMessage() {
        return super.getMessage();
    }
}
