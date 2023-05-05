package it.gov.pagopa.fdr.util;

import static jakarta.ws.rs.core.HttpHeaders.CONTENT_ENCODING;

import jakarta.ws.rs.ConstrainedTo;
import jakarta.ws.rs.RuntimeType;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.ext.ReaderInterceptor;
import jakarta.ws.rs.ext.ReaderInterceptorContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * Implementation based on {@see org.jboss.resteasy.plugins.interceptors.GZIPDecodingInterceptor}.
 */
@ConstrainedTo(RuntimeType.CLIENT)
@Provider
public class ClientGZIPDecodingInterceptor implements ReaderInterceptor {

  private static final String GZIP = "gzip";

  @Override
  public Object aroundReadFrom(ReaderInterceptorContext context)
      throws IOException, WebApplicationException {
    Object encoding = context.getHeaders().getFirst(CONTENT_ENCODING);
    if (encoding != null && encoding.toString().equalsIgnoreCase(GZIP)) {
      InputStream old = context.getInputStream();
      GZIPInputStream is = new GZIPInputStream(old);
      context.setInputStream(is);

      //      String text = new BufferedReader(
      //          new InputStreamReader(is, StandardCharsets.UTF_8))
      //          .lines()
      //          .collect(Collectors.joining("\n"));
      //
      //      //System.out.println(text);
      //      BufferedWriter writer = new BufferedWriter(new FileWriter("test.json"));
      //      writer.write(text);
      //      writer.close();

      Object response;
      try {
        response = context.proceed();
      } finally {
        context.setInputStream(old);
      }

      return response;
    } else {
      return context.proceed();
    }
  }
}
