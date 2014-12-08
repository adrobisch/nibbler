package de.androbit.nibbler;

import de.androbit.nibbler.converter.ContentConverter;
import de.androbit.nibbler.converter.ContentConverters;
import de.androbit.nibbler.converter.StringConverter;
import de.androbit.nibbler.http.MediaType;

import java.util.ArrayList;
import java.util.List;

public class RestHttpServerConfiguration {

  int port = 8080;
  String interfaceName = "0.0.0.0";
  int requestProcessingThreads = 50;

  List<RestService> restServices = new ArrayList<>();
  ContentConverters converters = new ContentConverters(new StringConverter());

  public int getPort() {
    return port;
  }

  public RestHttpServerConfiguration withPort(int port) {
    this.port = port;
    return this;
  }

  public String getInterface() {
    return interfaceName;
  }

  public RestHttpServerConfiguration withInterface(String interfaceName) {
    this.interfaceName = interfaceName;
    return this;
  }

  public int getRequestProcessingThreads() {
    return requestProcessingThreads;
  }

  public List<RestService> getRestServices() {
    return restServices;
  }

  public ContentConverters getConverters() {
    return converters;
  }

  public RestHttpServerConfiguration withRequestProcessingThreads(int requestProcessingThreads) {
    this.requestProcessingThreads = requestProcessingThreads;
    return this;
  }

  public RestHttpServerConfiguration withService(RestServiceBuilder restService) {
    this.restServices.add(restService.build());
    return this;
  }

  public RestHttpServerConfiguration withConverter(ContentConverter converter) {
    converters.setDefaultConverter(converter);
    return this;
  }

  public RestHttpServerConfiguration withTypeConverter(MediaType mediaType, ContentConverter converter) {
    converters.addConverter(mediaType, converter);
    return this;
  }

}
