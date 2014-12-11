package de.androbit.nibbler.http;

import de.androbit.nibbler.converter.ConvertibleOutput;
import de.androbit.nibbler.converter.TypedOutput;
import de.androbit.nibbler.handler.BodyHandlers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class DefaultRestResponse implements RestResponse {
  private Optional<ConvertibleOutput> convertibleBody = Optional.empty();
  private Optional<TypedOutput> rawBody = Optional.empty();

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
  public RestResponse body(String body) {
    return this.with(BodyHandlers.text(body));
  }

  @Override
  public RestResponse body(ConvertibleOutput body) {
    this.convertibleBody = Optional.of(body);
    return this;
  }

  @Override
  public RestResponse body(TypedOutput rawBody) {
    this.rawBody = Optional.of(rawBody);
    return this;
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
  public RestResponse with(Function<RestResponse, RestResponse> responseFunction) {
    return responseFunction.apply(this);
  }

  @Override
  public Optional<ConvertibleOutput> getConvertibleBody() {
    return convertibleBody;
  }

  @Override
  public Optional<TypedOutput> getRawBody() {
    return rawBody;
  }

  @Override
  public RestResponse immediate(boolean immediate) {
    this.immediate = immediate;
    return this;
  }
}
