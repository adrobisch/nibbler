package de.androbit.nibbler.http;

import java.util.function.BiFunction;

@FunctionalInterface
public interface RestRequestHandler extends BiFunction<RestRequest, RestResponse, RestResponse> {
  RestResponse handle(RestRequest restRequest, RestResponse restResponse);

  @Override
  default RestResponse apply(RestRequest restRequest, RestResponse restResponse) {
    return handle(restRequest, restResponse);
  }

  default RestRequestHandler filter(RestRequestHandler handler) {
    return (v, u) -> {
      if (u.isImmediate()) {
        return u;
      } else {
        return apply(v, handler.apply(v, u));
      }
    };
  }

  default RestRequestHandler process(RestRequestHandler handler) {
    return (v, u) -> {
      if (u.isImmediate()) {
        return u;
      } else {
        return handler.apply(v, apply(v, u));
      }
    };
  }
}