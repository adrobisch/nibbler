package de.androbit.nibbler.rest;

import com.jayway.restassured.RestAssured;
import de.androbit.nibbler.RestHttpServer;
import de.androbit.nibbler.RestServiceBuilder;
import de.androbit.nibbler.http.MediaType;
import org.junit.After;
import org.junit.Before;

import java.util.List;

public abstract class RestHttpServerTest {
  private RestHttpServer httpServer;

  @Before
  public void startServer() {
    int testServerPort = 9003;

    RestAssured.port = testServerPort;

    httpServer = new RestHttpServer()
      .withPort(testServerPort);

    for (RestServiceBuilder service: getBuilders()) {
      httpServer.withService(service);
    }

    httpServer.start();
  }

  @After
  public void stopServer() {
    httpServer.stop();
  }

  abstract List<RestServiceBuilder> getBuilders();
}