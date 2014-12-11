package de.androbit.nibbler.util;

import java.io.*;

public class IoUtil {
  public static byte[] loadBytes(InputStream inputStream) {
    try {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();

      int nRead;
      byte[] data = new byte[16384];

      while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
        buffer.write(data, 0, nRead);
      }
      buffer.flush();
      return buffer.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void writeText(File file, String content) {
    try {
      PrintWriter out = new PrintWriter(file.getAbsolutePath());
      out.print(content);
      out.close();
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public static String loadTextFile(File textFile) {
    try {
      try (FileInputStream fileStream = new FileInputStream(textFile)) {
        return new String(loadBytes(fileStream));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String streamToString(java.io.InputStream is) {
    return new String(loadBytes(is));
  }
}
