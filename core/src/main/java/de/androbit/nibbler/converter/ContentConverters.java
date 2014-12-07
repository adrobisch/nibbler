package de.androbit.nibbler.converter;

import de.androbit.nibbler.http.MediaType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ContentConverters {
  ContentConverter defaultConverter;
  Map<MediaType, ContentConverter> typeConverters = new HashMap<>();
  Map<Class, ContentConverter> converterClasses = new HashMap<>();

  public ContentConverters(ContentConverter defaultConverter) {
    setDefaultConverter(defaultConverter);
  }

  public Optional<ContentConverter> getConverter(Class<? extends ContentConverter> converterClass) {
    return Optional.ofNullable(converterClasses.get(converterClass));
  }

  public Optional<ContentConverter> getConverter(MediaType mediaType) {
    return Optional.ofNullable(typeConverters.get(mediaType));
  }

  public ContentConverters addConverter(MediaType mediaType, ContentConverter converter) {
    typeConverters.put(mediaType, converter);
    converterClasses.put(converter.getClass(), converter);
    return this;
  }

  public ContentConverter getDefaultConverter() {
    return defaultConverter;
  }

  public void setDefaultConverter(ContentConverter defaultConverter) {
    this.defaultConverter = defaultConverter;
    converterClasses.put(defaultConverter.getClass(), defaultConverter);
  }
}
