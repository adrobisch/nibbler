package de.androbit.nibbler.rest;

import de.androbit.nibbler.RestServiceBuilder;
import de.androbit.nibbler.http.Header;
import de.androbit.nibbler.http.MediaType;
import de.androbit.nibbler.http.RestRequestHandler;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class ContentNegotiationTest extends RestHttpServerTest {

  @Test
  public void shouldReturnPayloadIfTypeHandled() {
    given()
      .header("Accept", "application/json")
    .when()
      .get("/handles-json")
    .then()
      .assertThat()
      .statusCode(200)
      .body(equalTo("\"pong\""))
      .header("Content-Type", "application/json");
  }

  @Test
  public void shouldReturnStatusNotAcceptableIfTypeNotHandled() {
    given()
      .header("Accept", "application/json")
    .when()
      .get("/json-not-acceptable")
    .then()
      .assertThat()
      .statusCode(406);
  }

  @Override
  List<RestServiceBuilder> getBuilders() {
    RestServiceBuilder jsonService = new RestServiceBuilder() {
      @Override
      public void define() {
        RestRequestHandler jsonHandler = (in, out) -> {
          return out.body("\"pong\"")
            .header(Header.ContentType, MediaType.APPLICATION_JSON.contentType());
        };

        path("/handles-json")
          .get(handle(MediaType.APPLICATION_JSON, jsonHandler));

        path("/json-not-acceptable")
          .get(handle(MediaType.TEXT_PLAIN, (in, out) -> out));
      }
    };

    return Arrays.asList(jsonService);
  }
}
