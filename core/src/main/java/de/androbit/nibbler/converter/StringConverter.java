package de.androbit.nibbler.converter;

import de.androbit.nibbler.http.MediaType;
import de.androbit.nibbler.util.IoUtil;

public class StringConverter implements ContentConverter {
  @Override
  public <T> T fromBody(TypedInput body, Class<T> clazz) {
    if (clazz.getName().equals(String.class.getName())) {
      return (T) IoUtil.streamToString(body.getBodyStream());
    }
    throw new UnsupportedOperationException("converter supports strings only");
  }

  @Override
  public TypedOutput toBody(ConvertibleOutput object) {
    if (object == null) {
      throw new IllegalArgumentException("can't convert null to body");
    }

    return new TypedOutput(object.getOutput().toString().getBytes())
      .withMediaType(MediaType.TEXT_PLAIN);
  }
}
