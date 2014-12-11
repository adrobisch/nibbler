package de.androbit.nibbler.http;

import de.androbit.nibbler.converter.TypedInput;

import java.util.Optional;

public interface RestRequest {
  RestHttpMethod method();

  RequestPath path();

  Params pathParams();

  Optional<String> pathParam(String paramName);

  Params queryParams();

  Optional<String> queryParam(String paramName);

  String header(String name);

  TypedInput body();
  <T> T bodyAs(Class<T> clazz);
}
