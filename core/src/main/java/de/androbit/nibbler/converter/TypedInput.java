package de.androbit.nibbler.converter;

import de.androbit.nibbler.http.MediaType;

import java.io.InputStream;

public class TypedInput {
  final InputStream bodyStream;
  final MediaType mediaType;

  public TypedInput(InputStream bodyStream, MediaType mediaType) {
    this.bodyStream = bodyStream;
    this.mediaType = mediaType;
  }

  public InputStream getBodyStream() {
    return bodyStream;
  }

  public MediaType getMediaType() {
    return mediaType;
  }
}
