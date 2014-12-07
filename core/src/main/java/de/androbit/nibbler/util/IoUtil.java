package de.androbit.nibbler.util;

public class IoUtil {
  public static String streamToString(java.io.InputStream is) {
    java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
  }
}
