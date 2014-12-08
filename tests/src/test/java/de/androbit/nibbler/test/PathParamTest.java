package de.androbit.nibbler.test;

import de.androbit.nibbler.RestServiceBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class PathParamTest extends RestHttpServerTest {

  @Test
  public void shouldReturnParsePathParamAndEchoIt() {
    given()
    .when()
      .get("/echo-param/theParam?foo=1")
    .then()
      .assertThat()
      .statusCode(200)
      .body(equalTo("theParam"));
  }

  @Override
  List<RestServiceBuilder> getBuilders() {
    RestServiceBuilder jsonService = new RestServiceBuilder() {
      @Override
      public void define() {
        path("/echo-param/{param}")
          .get((request, response) -> {
            String paramValue = request.pathParams().get("param").get();
            return response.body(paramValue);
          });
      }
    };

    return Arrays.asList(jsonService);
  }
}
