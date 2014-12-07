package de.androbit.nibbler.http;

public enum MediaType {
  APPLICATION_JSON("application/json"),
  APPLICATION_OCTECT_STREAM("application/octet-stream"),
  TEXT_PLAIN("text/plain");

  private final String contentType;

  MediaType(String contentType) {
    this.contentType = contentType;
  }

  public String contentType() {
    return contentType;
  }
}
