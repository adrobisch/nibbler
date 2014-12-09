package de.androbit.nibbler.file;

import de.androbit.nibbler.http.MediaType;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class FileSupportTest {
  @Test
  public void shouldDetectMimeTypes() throws URISyntaxException, IOException {
    MediaType detectedType = FileSupport.getMediaType("/foo.png");
    assertEquals(MediaType.IMAGE_PNG, detectedType);

    MediaType javaScriptType = FileSupport.getMediaType("foo.js");
    assertEquals(MediaType.APPLICATION_JAVASCRIPT, javaScriptType);
  }
}