package de.androbit.nibbler;

import de.androbit.nibbler.dsl.RestServiceDsl;

public abstract class RestServiceBuilder extends RestServiceDsl {
  public abstract void define();

  public RestService build() {
    paths.clear();
    define();
    return new RestService(paths);
  }
}
