package de.androbit.nibbler.handler;

import de.androbit.nibbler.converter.TypedOutput;
import de.androbit.nibbler.http.MediaType;
import de.androbit.nibbler.http.RestResponse;

import java.util.function.Function;

public class BodyHandlers {
  public static Function<RestResponse, RestResponse> text(String theText) {
    return text(theText, MediaType.TEXT_PLAIN);
  }

  public static Function<RestResponse, RestResponse> text(String theText, MediaType mediaType) {
    return (response) -> response.body(new TypedOutput(theText.getBytes()).withMediaType(mediaType));
  }
}
