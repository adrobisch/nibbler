package de.androbit.nibbler.http;

import de.androbit.nibbler.converter.ConvertibleOutput;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultRestResponse implements RestResponse {
  private Optional<ConvertibleOutput> body = Optional.empty();
  private Map<Header, String> headers = new HashMap<>();
  private int status = 200;
  private boolean immediate = false;

  public DefaultRestResponse(int status, Map<Header, String> headers, boolean immediate) {
    this.status = status;
    this.headers = headers;
    this.immediate = immediate;
  }

  @Override
  public int getStatus() {
    return status;
  }

  @Override
  public RestResponse body(ConvertibleOutput body) {
    this.body = Optional.of(body);
    return this;
  }

  @Override
  public RestResponse header(Header header, String value) {
    headers.put(header, value);
    return this;
  }

  @Override
  public RestResponse status(int statusCode) {
    status = statusCode;
    return this;
  }

  @Override
  public RestResponse body(Object body) {
    return body(new ConvertibleOutput(body));
  }

  @Override
  public Map<Header, String> getHeaders() {
    return headers;
  }

  @Override
  public Optional<String> getHeader(Header header) {
    return Optional.ofNullable(headers.get(header));
  }

  @Override
  public boolean isImmediate() {
    return immediate;
  }

  @Override
  public Optional<ConvertibleOutput> getBody() {
    return body;
  }

  @Override
  public RestResponse immediate(boolean immediate) {
    this.immediate = immediate;
    return this;
  }
}
