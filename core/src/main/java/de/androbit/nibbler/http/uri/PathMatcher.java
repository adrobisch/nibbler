package de.androbit.nibbler.http.uri;

import java.util.HashMap;
import java.util.Map;

public class PathMatcher {
  public PathMatchResult match(String pathTemplate, String pathInstance) {
    String trimmedPath = removeLeadingAndTrailingSlash(pathInstance);
    String trimmedTemplate = removeLeadingAndTrailingSlash(pathTemplate);

    String[] pathSegments = trimmedPath.split("/");
    String[] templateSegments = trimmedTemplate.split("/");

    Map<String, String> pathParams = new HashMap<>();
    boolean segmentsMatch = matchSegments(pathSegments, templateSegments, pathParams);

    return new PathMatchResult(pathTemplate, pathInstance, segmentsMatch, pathParams);
  }

  protected boolean matchSegments(String[] pathSegments, String[] templateSegments, Map<String, String> pathParams) {
    for (int segmentIndex = 0; segmentIndex < templateSegments.length; segmentIndex++) {
      if (segmentIndex >= pathSegments.length) {
        return false;
      }
      String templateSegment = templateSegments[segmentIndex];
      String pathSegment = pathSegments[segmentIndex];

      if (isPathParam(templateSegment)) {
        String paramName = getParamName(templateSegment);
        pathParams.put(paramName, pathSegment);
      } else if (!templateSegment.equals(pathSegment)) {
        return false;
      }
    }
    return true;
  }

  protected boolean isPathParam(String templateSegment) {
    return templateSegment.startsWith("{") && templateSegment.endsWith("}");
  }

  protected String getParamName(String templateSegment) {
    return templateSegment.substring(1, templateSegment.length() -1 );
  }

  protected String removeLeadingAndTrailingSlash(String path) {
    path = path.trim();

    if (path.startsWith("/")) {
      path = path.substring(1, path.length());
    }

    if (path.endsWith("/")) {
      path = path.substring(0, path.length() - 1);
    }

    return path;
  }
}
