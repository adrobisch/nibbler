package de.androbit.nibbler.http;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Params {
  final Map<String, List<String>> params;

  public Params(Map<String, List<String>> params) {
    this.params = params;
  }

  public Optional<String> get(String name) {
    if (!params.containsKey(name)) {
      return Optional.empty();
    } else if (!params.get(name).isEmpty()) {
      return Optional.ofNullable(params.get(name).get(0));
    } else {
      return Optional.empty();
    }
  }
}
