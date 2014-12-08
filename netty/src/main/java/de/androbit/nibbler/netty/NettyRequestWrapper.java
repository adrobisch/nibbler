package de.androbit.nibbler.netty;

import de.androbit.nibbler.converter.ContentConverters;
import de.androbit.nibbler.converter.TypedInput;
import de.androbit.nibbler.http.*;
import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NettyRequestWrapper implements RestRequest {

  private final HttpServerRequest<ByteBuf> request;
  private final ContentConverters converters;
  private final ByteBuf content;
  private Map<String, String> pathParams;

  public NettyRequestWrapper(HttpServerRequest<ByteBuf> request, ByteBuf content, ContentConverters converters) {
    this.request = request;
    this.converters = converters;
    this.content = content;
  }

  @Override
  public RestHttpMethod method() {
    return RestHttpMethod.valueOf(request.getHttpMethod().name());
  }

  @Override
  public RequestPath path() {
    return new RequestPath(request.getPath());
  }

  @Override
  public Params pathParams() {
    HashMap<String, List<String>> paramsWithListValues = new HashMap<>();
    pathParams.forEach((key, value) -> paramsWithListValues.put(key, Arrays.asList(value)));

    return new Params(paramsWithListValues);
  }

  @Override
  public Params queryParams() {
    return new Params(request.getQueryParameters());
  }

  @Override
  public String header(String name) {
    return request.getHeaders().get(name);
  }

  @Override
  public TypedInput body() {
    return new TypedInput(getContentStream(), getContentType());
  }

  @Override
  public <T> T bodyAs(Class<T> clazz) {
    return convertBodyContent(getContentType(), clazz);
  }

  public <T> T convertBodyContent(MediaType contentType, Class<T> clazz) {
    return converters
      .getConverter(contentType)
      .orElse(converters.getDefaultConverter())
      .fromBody(new TypedInput(getContentStream(), contentType), clazz);
  }

  InputStream getContentStream() {
    byte[] bytes = new byte[content.readableBytes()];
    int readerIndex = content.readerIndex();
    content.getBytes(readerIndex, bytes);
    return new ByteArrayInputStream(bytes);
  }

  MediaType getContentType() {
    String header = header(Header.ContentType.name());
    return header != null ? MediaType.valueOf(header) : MediaType.APPLICATION_OCTECT_STREAM;
  }

  public NettyRequestWrapper withPathParams(Map<String, String> pathParams) {
    this.pathParams = pathParams;
    return this;
  }

}
