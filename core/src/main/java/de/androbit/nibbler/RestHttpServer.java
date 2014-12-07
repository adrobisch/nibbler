package de.androbit.nibbler;

import de.androbit.nibbler.converter.StringConverter;
import de.androbit.nibbler.dsl.PathDefinition;
import de.androbit.nibbler.converter.ContentConverter;
import de.androbit.nibbler.converter.ContentConverters;
import de.androbit.nibbler.http.MediaType;
import de.androbit.nibbler.netty.NettyServiceRequestHandler;
import de.androbit.nibbler.netty.RequestHandlerMatcher;
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

public class RestHttpServer {

  int port = 8080;
  String interfaceName = "0.0.0.0";
  int requestProcessingThreads = 50;

  Logger log = LoggerFactory.getLogger(RestHttpServer.class);

  List<RestService> restServices = new ArrayList<>();
  ContentConverters converters = new ContentConverters(new StringConverter());
  private HttpServer<ByteBuf, ByteBuf> nettyServer;

  public synchronized void start() {
    start(false);
  }

  public synchronized void startAndWait() {
    start(true);
  }

  public synchronized void start(boolean wait) {
    ServerBootstrap serverBootstrap = createServerBootstrap();

    HttpServerBuilder<ByteBuf, ByteBuf> httpServerBuilder =
      new HttpServerBuilder<>(serverBootstrap, port, createServiceRequestHandler())
        .pipelineConfigurator(PipelineConfigurators.<ByteBuf, ByteBuf>httpServerConfigurator())
        .withRequestProcessingThreads(requestProcessingThreads)
        .enableWireLogging(LogLevel.DEBUG);

    log.info(String.format("starting http server on port %s at interface %s, with %s processing threads ...", port, interfaceName, requestProcessingThreads));
    logServices();

    nettyServer = httpServerBuilder.build();
    if (wait) {
      nettyServer.startAndWait();
    } else {
      nettyServer.start();
    }
  }

  private void logServices() {
    restServices.forEach((service) -> {
      log.info(String.format("registered service: %s", service));
    });
  }

  public synchronized void stop() {
    try {
      nettyServer.shutdown();
      nettyServer.waitTillShutdown();
    } catch (InterruptedException e) {
      throw new RuntimeException();
    }
  }

  private ServerBootstrap createServerBootstrap() {
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    try {
      serverBootstrap.localAddress(InetAddress.getByName(interfaceName), port);
      return serverBootstrap;
    } catch (UnknownHostException e) {
      throw new RuntimeException();
    }
  }

  public int getPort() {
    return port;
  }

  public RestHttpServer withPort(int port) {
    this.port = port;
    return this;
  }

  public String getInterface() {
    return interfaceName;
  }

  public RestHttpServer withInterface(String interfaceName) {
    this.interfaceName = interfaceName;
    return this;
  }

  public int getRequestProcessingThreads() {
    return requestProcessingThreads;
  }

  public RestHttpServer withRequestProcessingThreads(int requestProcessingThreads) {
    this.requestProcessingThreads = requestProcessingThreads;
    return this;
  }

  NettyServiceRequestHandler createServiceRequestHandler() {
    List<PathDefinition> pathDefinitions = new ArrayList<>();
    for (RestService restService: restServices) {
      pathDefinitions.addAll(restService.paths);
    }
    return new NettyServiceRequestHandler(new RequestHandlerMatcher(pathDefinitions), converters);
  }

  public RestHttpServer withService(RestServiceBuilder restService) {
    this.restServices.add(restService.build());
    return this;
  }

  public RestHttpServer withConverter(ContentConverter converter) {
    converters.setDefaultConverter(converter);
    return this;
  }

  public RestHttpServer withTypeConverter(MediaType mediaType, ContentConverter converter) {
    converters.addConverter(mediaType, converter);
    return this;
  }

}
