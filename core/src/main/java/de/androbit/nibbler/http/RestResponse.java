package de.androbit.nibbler.http;

import de.androbit.nibbler.converter.ConvertibleOutput;
import de.androbit.nibbler.converter.TypedOutput;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public interface RestResponse {
  int getStatus();
  RestResponse status(int statusCode);

  RestResponse header(Header header, String value);
  Map<Header, String> getHeaders();
  Optional<String> getHeader(Header header);

  RestResponse body(String body);
  RestResponse body(ConvertibleOutput body);
  RestResponse body(TypedOutput rawBody);

  Optional<ConvertibleOutput> getConvertibleBody();
  Optional<TypedOutput> getRawBody();

  RestResponse immediate(boolean immediately);
  boolean isImmediate();

  RestResponse with(Function<RestResponse, RestResponse> responseFunction);
}
