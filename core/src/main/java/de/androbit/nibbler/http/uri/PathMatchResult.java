package de.androbit.nibbler.http.uri;

import java.util.Map;

public class PathMatchResult {
  final String pathTemplate;
  final String requestPath;
  final Map<String, String> pathParams;

  final boolean matches;

  public PathMatchResult(String pathTemplate, String requestPath, boolean matches, Map<String, String> pathParams) {
    this.pathTemplate = pathTemplate;
    this.requestPath = requestPath;
    this.matches = matches;
    this.pathParams = pathParams;
  }

  public boolean isMatch() {
    return matches;
  }

  public String getPathTemplate() {
    return pathTemplate;
  }

  public String getRequestPath() {
    return requestPath;
  }

  public Map<String, String> getPathParams() {
    return pathParams;
  }
}
