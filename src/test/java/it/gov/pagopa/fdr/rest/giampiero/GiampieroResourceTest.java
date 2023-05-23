package it.gov.pagopa.fdr.rest.giampiero;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.hamcrest.Matchers.containsString;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import it.gov.pagopa.fdr.Config;


import it.gov.pagopa.fdr.repository.giampiero.User;
import it.gov.pagopa.fdr.service.giampiero.GiampieroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openapi.quarkus.api_config_cache_json.model.ConfigDataV1;

import java.util.ArrayList;
import java.util.List;

@QuarkusTest
public class GiampieroResourceTest {

    private static final String nome = "Giovanni";

    private static final String cognome = "Grasso";

    private static final Long eta = 23L;

    private static final Header header = new Header("Content-Type", "application/json");


    private static String template = """
                {  
                    "nome": "%s",
                    "cognome": "%s",
                    "eta": %d
                }
                """;

    String response = """
      {"message":"Benvenuto %s!"}""";



    @InjectMock
    GiampieroService giampieroService;

    @BeforeEach
    public void setup() {
        Mockito.doNothing().when(giampieroService).save(null);
    }

    @Test
    @DisplayName("User create OK")
    public void testUserOk() {
    String url = "/user";
    String bodyFmt = template.formatted(nome, cognome, eta);
    String responseFmt = response.formatted(nome);

    given()
            .body(bodyFmt)
            .header(header)
            .when()
            .post(url)
            .then()
            .statusCode(201)
            .body(containsString(responseFmt));
    }


}
