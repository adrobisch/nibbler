package de.androbit.nibbler.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.androbit.nibbler.converter.ContentConverter;
import de.androbit.nibbler.converter.TypedInput;
import de.androbit.nibbler.converter.ConvertibleOutput;
import de.androbit.nibbler.converter.TypedOutput;
import de.androbit.nibbler.http.MediaType;

import java.io.IOException;

public class JacksonConverter implements ContentConverter {
  final ObjectMapper objectMapper;

  public JacksonConverter() {
    this.objectMapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public JacksonConverter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public <T> T fromBody(TypedInput typedInput, Class<T> clazz) {
    try {
      return objectMapper.readValue(typedInput.getBodyStream(), clazz);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public TypedOutput toBody(ConvertibleOutput object) {
    try {
      byte[] objectJson = objectMapper.writeValueAsBytes(object.getOutput());
      return new TypedOutput(objectJson).withMediaType(MediaType.APPLICATION_JSON);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
