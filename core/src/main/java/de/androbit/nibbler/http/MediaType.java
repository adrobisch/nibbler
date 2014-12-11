package de.androbit.nibbler.http;

public class MediaType {
  public static MediaType APPLICATION_JSON = new MediaType("application/json");
  public static MediaType APPLICATION_JAVASCRIPT = new MediaType("application/javascript");
  public static MediaType APPLICATION_XML = new MediaType("application/xml");
  public static MediaType APPLICATION_OCTET_STREAM = new MediaType("application/octet-stream");
  public static MediaType IMAGE_PNG = new MediaType("image/png");
  public static MediaType IMAGE_GIF = new MediaType("image/gif");
  public static MediaType IMAGE_JPEG = new MediaType("image/jpeg");
  public static MediaType TEXT_HTML = new MediaType("text/html");
  public static MediaType TEXT_CSS = new MediaType("text/css");
  public static MediaType TEXT_PLAIN = new MediaType("text/plain");
  public static MediaType TEXT_JAVASCRIPT = new MediaType("text/javascript");

  private final String contentType;

  public MediaType(String contentType) {
    this.contentType = contentType;
  }

  public String name() {
    return contentType;
  }

  public String contentType() {
    return contentType;
  }

  public static MediaType valueOf(String mimeString) {
    return new MediaType(mimeString);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MediaType mediaType = (MediaType) o;

    if (contentType != null ? !contentType.equals(mediaType.contentType) : mediaType.contentType != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return contentType != null ? contentType.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "MediaType{" +
      "contentType='" + contentType + '\'' +
      '}';
  }
}
