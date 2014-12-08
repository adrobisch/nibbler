package de.androbit.nibbler.http;

import de.androbit.nibbler.converter.TypedInput;

public interface RestRequest {
  RestHttpMethod method();

  RequestPath path();

  Params pathParams();
  Params queryParams();

  String header(String name);

  TypedInput body();
  <T> T bodyAs(Class<T> clazz);
}
