package de.androbit.nibbler.netty;

import de.androbit.nibbler.dsl.HandlerDefinition;
import de.androbit.nibbler.dsl.PathDefinition;
import de.androbit.nibbler.http.Header;
import de.androbit.nibbler.http.RestHttpMethod;
import de.androbit.nibbler.http.uri.PathMatchResult;
import de.androbit.nibbler.http.uri.PathMatcher;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RequestHandlerMatcher {

  List<PathDefinition> pathDefinitions;
  PathMatcher pathMatcher = new PathMatcher();

  public static class MatchingHandlers {
    List<FoundHandlerDefinition> methodHandlers;
    Optional<FoundHandlerDefinition> contentHandler;

    public MatchingHandlers(List<FoundHandlerDefinition> methodHandlers, Optional<FoundHandlerDefinition> contentHandler) {
      this.methodHandlers = methodHandlers;
      this.contentHandler = contentHandler;
    }

    public List<FoundHandlerDefinition> getMethodHandlers() {
      return methodHandlers;
    }

    public Optional<FoundHandlerDefinition> getContentHandler() {
      return contentHandler;
    }
  }

  public RequestHandlerMatcher(List<PathDefinition> pathDefinitions) {
    this.pathDefinitions = pathDefinitions;
  }

  public MatchingHandlers getMatchingHandlers(HttpServerRequest<ByteBuf> request) {
    Optional<String> acceptHeader = Optional.of(request.getHeaders().getHeader(Header.Accept.getName()));

    List<FoundHandlerDefinition> methodHandlers = getPathHandlers(request)
      .flatMap(matchingMethodHandlers(request.getHttpMethod()))
      .collect(Collectors.toList());

    Optional<FoundHandlerDefinition> contentHandler = getHandlerForContentType(acceptHeader, methodHandlers);
    return new MatchingHandlers(methodHandlers, contentHandler);
  }

  Function<MatchingPathDefinition, Stream<? extends FoundHandlerDefinition>> matchingMethodHandlers(HttpMethod httpMethod){
    return pathDefinition -> {
      RestHttpMethod restHttpMethod = RestHttpMethod.valueOf(httpMethod.name());

      return pathDefinition.getPathDefinition()
        .getMethodHandlers()
        .get(restHttpMethod)
        .stream()
        .filter(handlerWithMethod(restHttpMethod))
        .flatMap(handlerDefinition -> Stream.of(new FoundHandlerDefinition(pathDefinition.getMatchResult(), handlerDefinition)));
    };
  }

  Predicate<HandlerDefinition> handlerWithMethod(RestHttpMethod httpMethod) {
    return handlerDefinition -> handlerDefinition.getRestHttpMethod() == httpMethod;
  }

  private Optional<FoundHandlerDefinition> getHandlerForContentType(Optional<String> acceptHeader, List<FoundHandlerDefinition> methodHandlers) {
    Stream<FoundHandlerDefinition> contentHandlers = methodHandlers
      .stream()
      .filter(handler -> !acceptHeader.isPresent() ||
        allTypesAccepted(acceptHeader) ||
        handlerAccepts(acceptHeader, handler.getHandlerDefinition()));

    return contentHandlers.findFirst();
  }

  private boolean allTypesAccepted(Optional<String> acceptHeader) {
    return acceptHeader.get().contains("*/*");
  }

  private boolean handlerAccepts(Optional<String> acceptHeader, HandlerDefinition handler) {
    return handler.getHandledType().isPresent() &&
      acceptHeader.get().contains(handler.getHandledType().get().contentType());
  }

  private Stream<MatchingPathDefinition> getPathHandlers(HttpServerRequest<ByteBuf> request) {
    return pathDefinitions
      .stream()
      .flatMap(path -> {
        PathMatchResult matchResult = pathMatcher.match(path.getPathTemplate(), request.getPath());
        if (matchResult.isMatch()) {
          return Stream.of(new MatchingPathDefinition(path, matchResult));
        }else {
          return Stream.empty();
        }
      });
  }

  class MatchingPathDefinition {
    PathDefinition pathDefinition;
    PathMatchResult matchResult;

    MatchingPathDefinition(PathDefinition pathDefinition, PathMatchResult matchResult) {
      this.pathDefinition = pathDefinition;
      this.matchResult = matchResult;
    }

    public PathDefinition getPathDefinition() {
      return pathDefinition;
    }

    public PathMatchResult getMatchResult() {
      return matchResult;
    }
  }

}
