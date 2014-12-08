package de.androbit.nibbler.test;

import org.junit.Test;

public class RestServiceBuilderTest {
  @Test
  public void shouldBuildSimpleService() {
    TestServices.pingService.build();
  }
}