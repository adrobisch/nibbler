package de.androbit.nibbler.rest;

import org.junit.Test;

public class RestServiceBuilderTest {
  @Test
  public void shouldBuildSimpleService() {
    TestServices.pingService.build();
  }
}