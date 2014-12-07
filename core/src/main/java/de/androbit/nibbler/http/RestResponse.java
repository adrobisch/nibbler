package de.androbit.nibbler.http;

import de.androbit.nibbler.converter.ConvertibleOutput;

import java.util.Map;
import java.util.Optional;

public interface RestResponse {
  int getStatus();

  Map<Header, String> getHeaders();
  Optional<String> getHeader(Header header);
  boolean isImmediate();
  Optional<ConvertibleOutput> getBody();
  
  RestResponse status(int statusCode);
  RestResponse body(Object body);
  RestResponse body(ConvertibleOutput body);
  RestResponse header(Header header, String value);
  RestResponse immediate(boolean immediately);
}
