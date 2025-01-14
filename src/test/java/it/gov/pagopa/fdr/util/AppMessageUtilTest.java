package it.gov.pagopa.fdr.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class AppMessageUtilTest {

  @Test
  public void resourceBundle() {
    String str = AppMessageUtil.getMessage("app.description");
    assertEquals("FDR - Flussi di rendicontazione", str);
  }
}
