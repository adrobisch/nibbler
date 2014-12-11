package de.androbit.nibbler.handler;

import de.androbit.nibbler.http.Header;
import de.androbit.nibbler.http.RestResponse;

import java.util.function.Function;

public class RedirectHandlers {
  public Function<RestResponse, RestResponse> redirectPermanently(String targetUrl) {
    return (response) -> response.status(301).header(Header.Location, targetUrl);
  }
}
