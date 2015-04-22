package de.androbit.nibbler.test;

import de.androbit.nibbler.RestServiceBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;

public class GetTest extends RestHttpServerTest {
  @Test
  public void shouldReturnNotFoundOnUnhandledPath() {
    given()
    .when()
      .get("/pong")
    .then()
      .assertThat()
      .statusCode(404);
  }

  @Test
  public void shouldReturnMethodNotAllowedOnUnhandledMethod() {
    given()
      .when()
      .delete("/ping")
      .then()
      .assertThat()
      .statusCode(405);
  }

  @Override
  List<RestServiceBuilder> getBuilders() {
    return Arrays.asList(TestServices.pingService);
  }
}
