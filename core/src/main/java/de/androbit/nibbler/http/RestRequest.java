package de.androbit.nibbler.http;

import de.androbit.nibbler.converter.TypedInput;

import java.util.Map;

public interface RestRequest {
  RestHttpMethod method();

  String getPath();

  Params pathParams();
  Params queryParams();

  String header(String name);

  TypedInput body();
  <T> T bodyAs(Class<T> clazz);
}
