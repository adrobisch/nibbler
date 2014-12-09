package de.androbit.nibbler.test;

import de.androbit.nibbler.RestServiceBuilder;

public class TestServices {

  public static RestServiceBuilder pingService = new RestServiceBuilder() {
    @Override
    public void define() {
      path("/ping")
        .get((request, response) -> response.body("pong"))
        .post((request, response) -> {
          return response.body(request.bodyAs(String.class));
        });
    }
  };

}
