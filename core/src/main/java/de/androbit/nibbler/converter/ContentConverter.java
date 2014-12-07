package de.androbit.nibbler.converter;

public interface ContentConverter {
  <T> T fromBody(TypedInput body, Class<T> clazz);
  TypedOutput toBody(ConvertibleOutput object);
}
