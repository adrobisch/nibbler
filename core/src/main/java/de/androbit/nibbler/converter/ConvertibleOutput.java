package de.androbit.nibbler.converter;

import de.androbit.nibbler.http.MediaType;

import java.util.Optional;

public class ConvertibleOutput {
  Optional<Class<? extends ContentConverter>> converterClass = Optional.empty();
  Optional<MediaType> mediaType = Optional.empty();
  final Object output;

  public ConvertibleOutput(Object output) {
    this.output = output;
  }

  public Object getOutput() {
    return output;
  }

  public Optional<MediaType> getMediaType() {
    return mediaType;
  }

  public ConvertibleOutput withConverterClass(Class<? extends ContentConverter> converterClass) {
    this.converterClass = Optional.of(converterClass);
    return this;
  }

  public ConvertibleOutput withMediaType(MediaType mediaType) {
    this.mediaType = Optional.of(mediaType);
    return this;
  }

  public Optional<Class<? extends ContentConverter>> getConverterClass() {
    return converterClass;
  }
}
