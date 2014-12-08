package de.androbit.nibbler.netty;

import de.androbit.nibbler.RestHttpServer;
import de.androbit.nibbler.RestHttpServerConfiguration;
import de.androbit.nibbler.RestService;
import de.androbit.nibbler.converter.ContentConverters;
import de.androbit.nibbler.dsl.PathDefinition;
import de.androbit.nibbler.http.RequestHandlerMatcher;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.handler.logging.LogLevel;
import io.reactivex.netty.pipeline.PipelineConfigurators;
import io.reactivex.netty.protocol.http.server.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class NettyHttpServer implements RestHttpServer {
  private HttpServer<ByteBuf, ByteBuf> nettyServer;
  Logger log = LoggerFactory.getLogger(NettyHttpServer.class);

  @Override
  public void start(RestHttpServerConfiguration configuration) {
    nettyServer = createServer(configuration).start();
  }

  private HttpServer<ByteBuf, ByteBuf> createServer(RestHttpServerConfiguration configuration) {
    ServerBootstrap serverBootstrap = createServerBootstrap(configuration.getInterface(), configuration.getPort());

    HttpServerBuilder<ByteBuf, ByteBuf> httpServerBuilder =
      new HttpServerBuilder<>(serverBootstrap, configuration.getPort(),
        createServiceRequestHandler(configuration.getRestServices(), configuration.getConverters()))
        .pipelineConfigurator(PipelineConfigurators.<ByteBuf, ByteBuf>httpServerConfigurator())
        .withRequestProcessingThreads(configuration.getRequestProcessingThreads())
        .enableWireLogging(LogLevel.DEBUG);

    String message = "starting http server on port %s at interface %s, with %s processing threads ...";

    log.info(String.format(message, configuration.getPort(), configuration.getInterface(), configuration.getRequestProcessingThreads()));
    logServices(configuration.getRestServices());

    return httpServerBuilder.build();
  }

  @Override
  public void startAndWait(RestHttpServerConfiguration configuration) {
    nettyServer = createServer(configuration).start();
  }

  private void logServices(List<RestService> restServices) {
    restServices.forEach((service) -> {
      log.info(String.format("registered service: %s", service));
    });
  }

  @Override
  public synchronized void stop() {
    try {
      nettyServer.shutdown();
      nettyServer.waitTillShutdown();
    } catch (InterruptedException e) {
      throw new RuntimeException();
    }
  }

  private ServerBootstrap createServerBootstrap(String anInterface, int port) {
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    try {
      serverBootstrap.localAddress(InetAddress.getByName(anInterface), port);
      return serverBootstrap;
    } catch (UnknownHostException e) {
      throw new RuntimeException();
    }
  }

  NettyServiceRequestHandler createServiceRequestHandler(List<RestService> restServices, ContentConverters converters) {
    List<PathDefinition> pathDefinitions = new ArrayList<>();
    for (RestService restService: restServices) {
      pathDefinitions.addAll(restService.getPaths());
    }
    return new NettyServiceRequestHandler(new RequestHandlerMatcher(pathDefinitions), converters);
  }

}
