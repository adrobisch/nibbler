package de.androbit.nibbler.dsl;

import de.androbit.nibbler.http.MediaType;
import de.androbit.nibbler.http.RestRequestHandler;

import java.util.ArrayList;
import java.util.List;

public class RestServiceDsl {

  protected List<PathDefinition> paths = new ArrayList<>();

  public PathDefinition path(String pathTemplate) {
    return addPath(new PathDefinition(pathTemplate));
  }

  public RestRequestHandler handler(RestRequestHandler requestHandler) {
    return requestHandler;
  }

  protected HandlerDefinition handlerDefinition(RestRequestHandler restRequestHandler) {
    return new HandlerDefinition().withRequestHandler(restRequestHandler);
  }

  public HandlerDefinition type(MediaType mediaType, RestRequestHandler restRequestHandler) {
    return handlerDefinition(restRequestHandler).wirhHandledType(mediaType);
  }

  public PathDefinition addPath(PathDefinition pathDefinition) {
    paths.add(pathDefinition);
    return pathDefinition;
  }
}
