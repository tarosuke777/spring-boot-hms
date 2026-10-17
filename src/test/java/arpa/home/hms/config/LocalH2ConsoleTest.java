package arpa.home.hms.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = "spring.datasource.url=jdbc:h2:mem:h2console;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;NON_KEYWORDS=USER;MODE=MySQL")
@ActiveProfiles("local")
class LocalH2ConsoleTest {

  @Value("${local.server.port}")
  private int port;

  @Test
  void h2Console_ShouldBeAccessibleWithoutAuthentication() throws Exception {
    HttpRequest request = HttpRequest
        .newBuilder(URI.create("http://localhost:" + port + "/h2-console/")).GET().build();
    HttpResponse<String> response =
        HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    assertEquals(200, response.statusCode());
  }
}
