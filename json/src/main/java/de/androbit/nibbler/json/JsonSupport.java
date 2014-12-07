package de.androbit.nibbler.json;

import de.androbit.nibbler.converter.ConvertibleOutput;

public class JsonSupport {

  public static ConvertibleOutput json(Object value) {
    return new ConvertibleOutput(value)
      .withConverterClass(JacksonConverter.class);
  }

}
