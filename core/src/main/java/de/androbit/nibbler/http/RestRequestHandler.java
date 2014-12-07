package de.androbit.nibbler.http;

public interface RestRequestHandler {
  public RestResponse handle(RestRequest restRequest, RestResponse restResponse);
}
