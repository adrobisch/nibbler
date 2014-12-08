package de.androbit.nibbler;

public interface RestHttpServer {
  public void start(RestHttpServerConfiguration configuration);
  public void startAndWait(RestHttpServerConfiguration configuration);
  public void stop();
}
