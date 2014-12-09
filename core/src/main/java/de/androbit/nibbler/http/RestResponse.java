package de.androbit.nibbler.http;

import de.androbit.nibbler.converter.ConvertibleOutput;
import de.androbit.nibbler.converter.TypedOutput;

import java.util.Map;
import java.util.Optional;

public interface RestResponse {
  int getStatus();

  Map<Header, String> getHeaders();
  Optional<String> getHeader(Header header);
  boolean isImmediate();
  Optional<ConvertibleOutput> getConvertibleBody();
  Optional<TypedOutput> getRawBody();

  RestResponse status(int statusCode);
  RestResponse body(Object body);
  RestResponse body(ConvertibleOutput body);
  RestResponse body(TypedOutput rawBody);
  RestResponse header(Header header, String value);
  RestResponse immediate(boolean immediately);
}
