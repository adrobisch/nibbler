package de.androbit.nibbler.dsl;

import de.androbit.nibbler.http.MediaType;
import de.androbit.nibbler.http.RestHttpMethod;
import de.androbit.nibbler.http.RestRequestHandler;

import java.util.Optional;

public class HandlerDefinition {
  RestHttpMethod restHttpMethod;
  RestRequestHandler requestHandler;

  Optional<MediaType> handledType = Optional.empty();

  public RestHttpMethod getRestHttpMethod() {
    return restHttpMethod;
  }

  public HandlerDefinition withHttpMethod(RestHttpMethod restHttpMethod) {
    this.restHttpMethod = restHttpMethod;
    return this;
  }

  public RestRequestHandler getRequestHandler() {
    return requestHandler;
  }

  public HandlerDefinition withRequestHandler(RestRequestHandler requestHandler) {
    this.requestHandler = requestHandler;
    return this;
  }

  public Optional<MediaType> getHandledType() {
    return handledType;
  }

  public HandlerDefinition wirhHandledType(MediaType handledType) {
    this.handledType = Optional.of(handledType);
    return this;
  }

  @Override
  public String toString() {
    return "HandlerDefinition{" +
      "restHttpMethod=" + restHttpMethod +
      ", handledType=" + handledType +
      '}';
  }
}
