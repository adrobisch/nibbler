package de.androbit.nibbler.netty;

import de.androbit.nibbler.converter.ContentConverter;
import de.androbit.nibbler.converter.ContentConverters;
import de.androbit.nibbler.converter.ConvertibleOutput;
import de.androbit.nibbler.converter.TypedOutput;
import de.androbit.nibbler.dsl.HandlerDefinition;
import de.androbit.nibbler.http.Header;
import de.androbit.nibbler.http.MediaType;
import de.androbit.nibbler.http.MediaTypes;
import de.androbit.nibbler.http.RestResponse;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;

import java.util.Optional;

public class ResponseWriter {

  final ContentConverters converters;

  public ResponseWriter(ContentConverters converters) {
    this.converters = converters;
  }

  public RestResponse writeResponse(RestResponse serviceResponse, HttpServerResponse<ByteBuf> response, HandlerDefinition handlerDefinition) {
    Optional<TypedOutput> responseBody = getResponseBody(serviceResponse);
    RestResponse serviceResponseWithContent = validateContentType(serviceResponse, handlerDefinition.getHandledType(), responseBody);

    response.setStatus(HttpResponseStatus.valueOf(serviceResponseWithContent.getStatus()));

    writeToResponse(response, responseBody);

    serviceResponseWithContent.getHeaders()
      .forEach((header, value) -> response.getHeaders().set(header.getName(), value));

    return serviceResponseWithContent;
  }

  protected RestResponse validateContentType(RestResponse response, Optional<MediaType> handledType, Optional<TypedOutput> responseBody) {
    if (handledType.isPresent() && !responseBody.isPresent()) {
      return respondNotAcceptable(response, handledType, responseBody);
    } else {
      MediaType responseType = MediaTypes
          .from(response.getHeader(Header.ContentType)
          .orElse(MediaType.APPLICATION_OCTECT_STREAM.contentType()));

      if (handledType.isPresent() &&
        !(responseType == handledType.get())) {
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
    Optional<ConvertibleOutput> responseBody = serviceResponse.getBody();
    if (responseBody.isPresent()) {
      return Optional.of(getOutput(responseBody.get()));
    }
    return Optional.empty();
  }

  private TypedOutput getOutput(ConvertibleOutput responseBody) {
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
      response.getHeaders().set(Header.ContentType.getName(), typedOutput.get().getMediaType().contentType());
    }
  }
}
