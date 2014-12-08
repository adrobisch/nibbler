package de.androbit.nibbler.test;

import de.androbit.nibbler.RestServiceBuilder;

public class TestServices {

  static class PostObject {
    String value;

    public String getValue() {
      return value;
    }
  }

  public static RestServiceBuilder pingService = new RestServiceBuilder() {
    @Override
    public void define() {
      path("/ping")
        .get((request, response) -> response.body("pong"))
        .post((request, response) -> {
          return response.body(request.bodyAs(PostObject.class));
        });
    }
  };

}
