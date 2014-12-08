package de.androbit.nibbler.http;

import de.androbit.nibbler.dsl.HandlerDefinition;
import de.androbit.nibbler.http.uri.PathMatchResult;

public class FoundHandlerDefinition {
  PathMatchResult matchResult;
  HandlerDefinition handlerDefinition;

  FoundHandlerDefinition(PathMatchResult matchResult, HandlerDefinition handlerDefinition) {
    this.matchResult = matchResult;
    this.handlerDefinition = handlerDefinition;
  }

  public PathMatchResult getMatchResult() {
    return matchResult;
  }

  public HandlerDefinition getHandlerDefinition() {
    return handlerDefinition;
  }
}
