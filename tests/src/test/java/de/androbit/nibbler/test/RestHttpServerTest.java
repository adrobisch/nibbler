package de.androbit.nibbler.test;

import com.jayway.restassured.RestAssured;
import de.androbit.nibbler.RestHttpServer;
import de.androbit.nibbler.RestHttpServerConfiguration;
import de.androbit.nibbler.RestServiceBuilder;
import de.androbit.nibbler.test.spi.RestHttpServerProvider;
import org.junit.After;
import org.junit.Before;

import java.util.List;
import java.util.ServiceLoader;

public abstract class RestHttpServerTest {
  private RestHttpServer httpServer;

  @Before
  public void startServer() {
    int testServerPort = 9003;

    RestAssured.port = testServerPort;

    httpServer = createServer();

    RestHttpServerConfiguration configuration = createConfiguration(testServerPort);

    httpServer.start(configuration);
  }

  private RestHttpServerConfiguration createConfiguration(int testServerPort) {
    RestHttpServerConfiguration configuration = new RestHttpServerConfiguration()
      .withPort(testServerPort);

    getBuilders().forEach(configuration::withService);

    return configuration;
  }

  @After
  public void stopServer() {
    httpServer.stop();
  }

  RestHttpServer createServer() {
    ServiceLoader<RestHttpServerProvider> httpServerProvider = ServiceLoader.load(RestHttpServerProvider.class);
    return httpServerProvider.iterator().next().newHttpServer();
  }

  abstract List<RestServiceBuilder> getBuilders();
}