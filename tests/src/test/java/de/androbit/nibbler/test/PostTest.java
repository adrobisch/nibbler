package de.androbit.nibbler.test;

import de.androbit.nibbler.RestServiceBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class PostTest extends RestHttpServerTest {
  @Test
  public void shouldReturnPostBodyInResponse() {
    given()
      .body("{ \"value\": \"foo\" }")
    .when()
      .post("/ping")
    .then()
      .assertThat()
      .statusCode(200)
      .body(equalTo("{ \"value\": \"foo\" }"))
      .header("Content-Type", "text/plain");
  }

  @Override
  List<RestServiceBuilder> getBuilders() {
    return Arrays.asList(TestServices.pingService);
  }
}
