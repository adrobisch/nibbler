package de.androbit.nibbler.file;

import de.androbit.nibbler.converter.TypedOutput;
import de.androbit.nibbler.http.MediaType;
import de.androbit.nibbler.http.MediaTypes;
import de.androbit.nibbler.http.RestResponse;
import org.apache.tika.Tika;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileSupport {
  // Tika is thread safe
  private static Tika tika = new Tika();

  public static RestResponse classPathResource(String resourcePath, RestResponse restResponse) {
    InputStream resourceStream = FileSupport.class.getResourceAsStream(resourcePath);
    MediaType from = getMediaType(resourcePath);
    TypedOutput output = new TypedOutput(readBytes(resourceStream)).withMediaType(from);
    restResponse.body(output);
    return restResponse;
  }

  public static MediaType getMediaType(String filePath) {
    String detectedType = tika.detect(filePath);
    return MediaTypes.from(detectedType);
  }

  private static byte[] readBytes(java.io.InputStream stream) {
    try (InputStream is = stream) {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();

      int nRead;
      byte[] data = new byte[16384];

      while ((nRead = is.read(data, 0, data.length)) != -1) {
        buffer.write(data, 0, nRead);
      }
      buffer.flush();
      return buffer.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException();
    }
  }

}
