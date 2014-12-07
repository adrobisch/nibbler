package de.androbit.nibbler.converter;

import de.androbit.nibbler.http.MediaType;

public class TypedOutput {
  final byte[] output;
  MediaType mediaType;

  public TypedOutput(byte[] output) {
    this.output = output;
  }

  public byte[] getOutput() {
    return output;
  }

  public MediaType getMediaType() {
    return mediaType;
  }

  public TypedOutput withMediaType(MediaType mediaType) {
    this.mediaType = mediaType;
    return this;
  }

}
