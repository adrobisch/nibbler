package de.androbit.nibbler.http;

import de.androbit.nibbler.dsl.HandlerDefinition;
import de.androbit.nibbler.dsl.PathDefinition;
import de.androbit.nibbler.http.uri.PathMatchResult;
import de.androbit.nibbler.http.uri.PathMatcher;

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
    private List<MatchingPathDefinition> matchingPathHandlers;
    List<FoundHandlerDefinition> methodHandlers;
    Optional<FoundHandlerDefinition> contentHandler;

    public MatchingHandlers(List<MatchingPathDefinition> matchingPathHandlers, List<FoundHandlerDefinition> methodHandlers, Optional<FoundHandlerDefinition> contentHandler) {
      this.matchingPathHandlers = matchingPathHandlers;
      this.methodHandlers = methodHandlers;
      this.contentHandler = contentHandler;
    }

    public List<MatchingPathDefinition> getMatchingPathHandlers() {
      return matchingPathHandlers;
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

  public MatchingHandlers getMatchingHandlers(RestRequest request) {
    Optional<String> acceptHeader = Optional.ofNullable(request.header(Header.Accept.name()));

    List<MatchingPathDefinition> matchingPathHandlers = getPathHandlers(request.path().value())
      .collect(Collectors.toList());

    List<FoundHandlerDefinition> methodHandlers = matchingPathHandlers.stream().flatMap(matchingMethodHandlers(request.method()))
      .collect(Collectors.toList());

    Optional<FoundHandlerDefinition> contentHandler = getHandlerForContentType(acceptHeader, methodHandlers);
    return new MatchingHandlers(matchingPathHandlers, methodHandlers, contentHandler);
  }

  Function<MatchingPathDefinition, Stream<? extends FoundHandlerDefinition>> matchingMethodHandlers(RestHttpMethod restHttpMethod){
    return pathDefinition -> {
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

  private Stream<MatchingPathDefinition> getPathHandlers(String requestPath) {
    return pathDefinitions
      .stream()
      .flatMap(path -> {
        PathMatchResult matchResult = pathMatcher.match(path.getPathTemplate(), requestPath);
        if (matchResult.isMatch()) {
          return Stream.of(new MatchingPathDefinition(path, matchResult));
        } else {
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
