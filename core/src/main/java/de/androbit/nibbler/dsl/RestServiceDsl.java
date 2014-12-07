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

  public HandlerDefinition handle(RestRequestHandler restRequestHandler) {
    return new HandlerDefinition().withRequestHandler(restRequestHandler);
  }

  public HandlerDefinition handle(MediaType mediaType, RestRequestHandler restRequestHandler) {
    return handle(restRequestHandler).wirhHandledType(mediaType);
  }

  public HandlerDefinition before(RestRequestHandler restRequestHandler, RestRequestHandler transformer) {
    return before(new HandlerDefinition().withRequestHandler(restRequestHandler), transformer);
  }

  public HandlerDefinition before(HandlerDefinition handlerDefinition, RestRequestHandler transformer) {
    return handlerDefinition.filter(transformer);
  }

  public HandlerDefinition after(RestRequestHandler restRequestHandler, RestRequestHandler transformer) {
    return after(new HandlerDefinition().withRequestHandler(restRequestHandler), transformer);
  }

  public HandlerDefinition after(HandlerDefinition handlerDefinition, RestRequestHandler transformer) {
    return handlerDefinition.transform(transformer);
  }

  public PathDefinition addPath(PathDefinition pathDefinition) {
    paths.add(pathDefinition);
    return pathDefinition;
  }
}
