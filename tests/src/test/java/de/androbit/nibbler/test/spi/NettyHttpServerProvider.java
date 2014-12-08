package de.androbit.nibbler.test.spi;

import de.androbit.nibbler.RestHttpServer;
import de.androbit.nibbler.netty.NettyHttpServer;

public class NettyHttpServerProvider implements RestHttpServerProvider {
  @Override
  public RestHttpServer newHttpServer() {
    return new NettyHttpServer();
  }
}
