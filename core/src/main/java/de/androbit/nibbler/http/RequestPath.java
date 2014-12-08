package de.androbit.nibbler.http;

public class RequestPath {
  String path;

  public RequestPath(String path) {
    this.path = path;
  }

  public String value() {
    return path;
  }

  public String suffixAfter(String prefix) {
    int prefixIndex = path.indexOf(prefix);
    if (prefixIndex == -1) {
      throw new IllegalArgumentException("no such prefix in request path: " + prefix);
    } else {
      return path.substring(prefixIndex + prefix.length());
    }
  }
}
