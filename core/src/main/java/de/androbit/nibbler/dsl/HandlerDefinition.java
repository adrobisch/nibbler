package de.androbit.nibbler.dsl;

import de.androbit.nibbler.http.MediaType;
import de.androbit.nibbler.http.RestHttpMethod;
import de.androbit.nibbler.http.RestRequestHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerDefinition {
  RestHttpMethod restHttpMethod;
  RestRequestHandler requestHandler;

  Optional<MediaType> handledType = Optional.empty();

  List<RestRequestHandler> beforeHandlers = new ArrayList<>();
  List<RestRequestHandler> afterHandlers = new ArrayList<>();

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

  public List<RestRequestHandler> getBeforeHandlers() {
    return beforeHandlers;
  }

  public List<RestRequestHandler> getAfterHandlers() {
    return afterHandlers;
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

  public HandlerDefinition filter(RestRequestHandler transformer) {
    this.beforeHandlers.add(transformer);
    return this;
  }

  public HandlerDefinition transform(RestRequestHandler transformer) {
    this.afterHandlers.add(transformer);
    return this;
  }
}
