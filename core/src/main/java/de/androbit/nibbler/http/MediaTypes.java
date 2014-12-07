package de.androbit.nibbler.http;

import java.util.HashMap;

public class MediaTypes {

  static HashMap<String, MediaType> typeByName = createMediaTypeMap();

  private static HashMap createMediaTypeMap() {
    HashMap types = new HashMap<String, MediaType>();
    for (MediaType mediaType : MediaType.values()) {
      types.put(mediaType.contentType(), mediaType);
    }
    return types;
  }

  public static MediaType from(String mediaType) {
    return typeByName.get(mediaType);
  }

}
