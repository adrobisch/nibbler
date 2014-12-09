package de.androbit.nibbler.converter;

import de.androbit.nibbler.http.MediaType;

import java.util.Optional;

public class TypedOutput {
  final byte[] output;
  Optional<MediaType> mediaType = Optional.empty();

  public TypedOutput(byte[] output) {
    this.output = output;
  }

  public byte[] getOutput() {
    return output;
  }

  public Optional<MediaType> getMediaType() {
    return mediaType;
  }

  public TypedOutput withMediaType(MediaType mediaType) {
    this.mediaType = Optional.ofNullable(mediaType);
    return this;
  }

}
