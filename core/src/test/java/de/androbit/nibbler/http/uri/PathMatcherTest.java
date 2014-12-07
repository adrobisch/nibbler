package de.androbit.nibbler.http.uri;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PathMatcherTest {
  @Test
  public void shouldMatchPathParams() {
    PathMatchResult matchResult = new PathMatcher().match("/foo/{param}", "/foo/123");

    assertThat(matchResult.isMatch()).isTrue();
    assertThat(matchResult.getPathParams().get("param")).isEqualTo("123");
  }

  @Test
  public void shouldMatchMultiplePathParams() {
    PathMatchResult matchResult = new PathMatcher().match("/foo/{param}/{param2}", "/foo/123/abc");

    assertThat(matchResult.isMatch()).isTrue();
    assertThat(matchResult.getPathParams().get("param")).isEqualTo("123");
    assertThat(matchResult.getPathParams().get("param2")).isEqualTo("abc");
  }

  @Test
  public void shouldIgnoreTrailingSlash() {
    PathMatchResult trailingInTemplateAndPath = new PathMatcher().match("/foo/{param}/", "/foo/123/");
    PathMatchResult trailingInTemplate = new PathMatcher().match("/foo/{param}/", "/foo/123");
    PathMatchResult trailingInPath = new PathMatcher().match("/foo/{param}", "/foo/123/");

    assertMatch(trailingInTemplateAndPath);
    assertMatch(trailingInTemplate);
    assertMatch(trailingInPath);
  }

  @Test
  public void shouldIgnoreLeadingSlash() {
    PathMatchResult leadingInTemplateAndPath = new PathMatcher().match("/foo/{param}", "/foo/123");
    PathMatchResult leadingInTempalte = new PathMatcher().match("/foo/{param}", "foo/123");
    PathMatchResult leadingInPath = new PathMatcher().match("foo/{param}", "/foo/123");

    assertMatch(leadingInTemplateAndPath);
    assertMatch(leadingInTempalte);
    assertMatch(leadingInPath);
  }

  private void assertMatch(PathMatchResult matchResult) {
    assertThat(matchResult.isMatch()).isTrue();
    assertThat(matchResult.getPathParams().get("param")).isEqualTo("123");
  }
}