package de.androbit.nibbler.netty;

import de.androbit.nibbler.converter.ContentConverter;
import de.androbit.nibbler.converter.ContentConverters;
import de.androbit.nibbler.converter.ConvertibleOutput;
import de.androbit.nibbler.converter.TypedOutput;
import de.androbit.nibbler.dsl.HandlerDefinition;
import de.androbit.nibbler.http.Header;
import de.androbit.nibbler.http.MediaType;
import de.androbit.nibbler.http.RestResponse;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;

import java.util.Optional;

public class NettyResponseWriter {

  final ContentConverters converters;

  public NettyResponseWriter(ContentConverters converters) {
    this.converters = converters;
  }

  public RestResponse writeResponse(RestResponse serviceResponse, HttpServerResponse<ByteBuf> response, HandlerDefinition handlerDefinition) {
    Optional<TypedOutput> responseBody = getResponseBody(serviceResponse);
    RestResponse serviceResponseWithContent = validateContentType(serviceResponse, handlerDefinition.getHandledType(), responseBody);

    response.setStatus(HttpResponseStatus.valueOf(serviceResponseWithContent.getStatus()));

    writeToResponse(response, responseBody);

    serviceResponseWithContent.getHeaders()
      .forEach((header, value) -> response.getHeaders().set(header.name(), value));

    return serviceResponseWithContent;
  }

  protected RestResponse validateContentType(RestResponse response, Optional<MediaType> handledType, Optional<TypedOutput> responseBody) {
    if (handledType.isPresent() && !responseBody.isPresent()) {
      return respondNotAcceptable(response, handledType, responseBody);
    } else {
      MediaType responseType = MediaType
          .valueOf(response.getHeader(Header.ContentType)
            .orElse(MediaType.APPLICATION_OCTET_STREAM.contentType()));

      if (handledType.isPresent() &&
        !responseType.equals(handledType.get())) {
        return respondNotAcceptable(response, handledType, responseBody);
      }
    }
    return response;
  }

  private RestResponse respondNotAcceptable(RestResponse restResponse, Optional<MediaType> handledType, Optional<TypedOutput> responseBody) {
    String unacceptableMessage = String.format("tried to produce Content-Type %s, when %s should be produced",
      responseBody, handledType);

    return restResponse.body(new ConvertibleOutput(unacceptableMessage).withMediaType(MediaType.TEXT_PLAIN)).status(HttpResponseStatus.NOT_ACCEPTABLE.code());
  }

  protected Optional<TypedOutput> getResponseBody(RestResponse serviceResponse) {
    if (serviceResponse.getRawBody().isPresent()) {
      return serviceResponse.getRawBody();
    } else if (serviceResponse.getConvertibleBody().isPresent()) {
      return Optional.of(convert(serviceResponse.getConvertibleBody().get()));
    }
    return Optional.empty();
  }

  private TypedOutput convert(ConvertibleOutput responseBody) {
    if(responseBody.getConverterClass().isPresent()){
      Class<? extends ContentConverter> converterClass = responseBody.getConverterClass().get();
      return convertOutput(responseBody, converters.getConverter(converterClass).get());
    } else if (responseBody.getMediaType().isPresent()) {
      ContentConverter mediaTypeConverter = converters.getConverter(responseBody.getMediaType().get()).get();
      return convertOutput(responseBody, mediaTypeConverter);
    } else {
      return convertOutput(responseBody, converters.getDefaultConverter());
    }
  }

  private TypedOutput convertOutput(ConvertibleOutput responseBody, ContentConverter converter) {
    return converter.toBody(responseBody);
  }

  private void writeToResponse(HttpServerResponse<ByteBuf> response, Optional<TypedOutput> typedOutput) {
    if (typedOutput.isPresent()) {
      response.writeBytes(typedOutput.get().getOutput());
      String contentType = typedOutput.get().getMediaType().orElse(MediaType.APPLICATION_OCTET_STREAM).contentType();
      response.getHeaders().set(Header.ContentType.name(), contentType);
    }
  }
}
