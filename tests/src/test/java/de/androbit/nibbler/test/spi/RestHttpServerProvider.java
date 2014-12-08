package de.androbit.nibbler.test.spi;

import de.androbit.nibbler.RestHttpServer;

public interface RestHttpServerProvider {
  RestHttpServer newHttpServer();
}
