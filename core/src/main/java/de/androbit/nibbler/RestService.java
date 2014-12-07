package de.androbit.nibbler;

import de.androbit.nibbler.dsl.PathDefinition;

import java.util.List;

public class RestService {

  final List<PathDefinition> paths;

  public RestService(List<PathDefinition> paths) {
    this.paths = paths;
  }

  @Override
  public String toString() {
    return "RestService{" +
      "paths=" + paths +
      '}';
  }
}
