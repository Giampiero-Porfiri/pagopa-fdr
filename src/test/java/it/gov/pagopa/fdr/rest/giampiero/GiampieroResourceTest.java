package it.gov.pagopa.fdr.rest.giampiero;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import io.restassured.http.Header;

import it.gov.pagopa.fdr.service.giampiero.GiampieroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;


@QuarkusTest
public class GiampieroResourceTest {

    private static final String nome = "Giovanni";

    private static final String cognome = "Grasso";

    private static final LocalDate nascita =LocalDate.of(2000,10,21);

    private static final Long eta = 23L;

    private static final Header header = new Header("Content-Type", "application/json");


    private static String template = """
                {  
                    "nome": "%s",
                    "cognome": "%s",
                    "nascita":"%s",
                    "eta": %d
                }
                """;

    String response = """
      {"message":"Benvenuto %s!"}""";

    String response2 = """
      {"message":"Ho modificato %s"}""";

    //String response3 = """
    //  {"message":"Ho cancellato %s"}""";





    @InjectMock
    GiampieroService giampieroService;

    @BeforeEach
    public void setup() {
        Mockito.doNothing().when(giampieroService).save(null);
        Mockito.doNothing().when(giampieroService).update(null, null);
        Mockito.doNothing().when(giampieroService).delete(null);
    }

    @Test
    @DisplayName("User create OK")
    public void testUserOk() {
    String url = "/user";
    String bodyFmt = template.formatted(nome, cognome, nascita, eta);
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

    @Test
    @DisplayName("User Update OK")
    public void updateUserOk() {
        String url = "/user/" + nome;
        String bodyFmt = template.formatted(nome, cognome, nascita, eta);
        String responseFmt = response2.formatted(nome);

        given()
                .body(bodyFmt)
                .header(header)
                .when()
                .put(url)
                .then()
                .statusCode(200)
                .body(containsString(responseFmt));

    }

    @Test
    @DisplayName("User Delete OK")
    public void deleteUserOK() {
        String url = "/user/" + nome;
        String bodyFmt = template.formatted(nome, cognome, nascita, eta);


        given()
                .body(bodyFmt)
                .header(header)
                .when()
                .delete(url)
                .then()
                .statusCode(204);

    }

}
