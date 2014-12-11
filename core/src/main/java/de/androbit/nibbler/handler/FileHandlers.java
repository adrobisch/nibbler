package de.androbit.nibbler.handler;

import de.androbit.nibbler.converter.TypedOutput;
import de.androbit.nibbler.http.MediaType;
import de.androbit.nibbler.http.RestResponse;
import de.androbit.nibbler.util.IoUtil;

import java.io.File;
import java.util.function.Function;

public class FileHandlers {
  public static Function<RestResponse, RestResponse> classPathResource(String path, MediaType mediaType) {
    return (response) -> response.body(new TypedOutput(IoUtil.loadBytes(FileHandlers.class.getResourceAsStream(path))).withMediaType(mediaType));
  }

  public static Function<RestResponse, RestResponse> fileContent(File file) {
    return fileContent(file, MediaType.TEXT_PLAIN);
  }

  public static Function<RestResponse, RestResponse> fileContent(File file, MediaType mediaType) {
    return (response) -> response.body(new TypedOutput(IoUtil.loadTextFile(file).getBytes()).withMediaType(mediaType));
  }
}
