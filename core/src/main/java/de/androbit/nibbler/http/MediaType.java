package de.androbit.nibbler.http;

public enum MediaType {
  APPLICATION_JSON("application/json"),
  APPLICATION_JAVASCRIPT("application/javascript"),
  APPLICATION_XML("application/xml"),
  APPLICATION_OCTET_STREAM("application/octet-stream"),
  IMAGE_PNG("image/png"),
  IMAGE_GIF("image/gif"),
  IMAGE_JPEG("image/jpeg"),
  TEXT_HTML("text/html"),
  TEXT_CSS("text/css"),
  TEXT_PLAIN("text/plain"),
  TEXT_JAVASCRIPT("text/javascript");

  private final String contentType;

  MediaType(String contentType) {
    this.contentType = contentType;
  }

  public String contentType() {
    return contentType;
  }
}
