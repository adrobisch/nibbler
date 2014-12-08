package de.androbit.nibbler.http;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestPathTest {
  @Test
  public void shouldReturnValue() {
    RequestPath path = new RequestPath("prefix/suffix");
    assertThat(path.value()).isEqualTo("prefix/suffix");
  }

  @Test
  public void shouldReturnSuffix() {
    String suffix = new RequestPath("prefix/suffix").suffixAfter("prefix");
    assertThat(suffix).isEqualTo("/suffix");
  }
}