package de.androbit.nibbler.json;

import de.androbit.nibbler.converter.ConvertibleOutput;
import de.androbit.nibbler.http.RestResponse;

import java.util.function.Function;

public class JsonSupport {

  public static Function<RestResponse, RestResponse> json(Object value) {
    return (response) ->  response.body(asJson(value));
  }

  public static ConvertibleOutput asJson(Object value) {
    return new ConvertibleOutput(value)
      .withConverterClass(JacksonConverter.class);
  }

}
