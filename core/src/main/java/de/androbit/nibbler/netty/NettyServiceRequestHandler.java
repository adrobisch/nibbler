package de.androbit.nibbler.netty;

import de.androbit.nibbler.util.StacktraceUtil;
import de.androbit.nibbler.converter.ContentConverters;
import de.androbit.nibbler.dsl.HandlerDefinition;
import de.androbit.nibbler.http.*;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.reactivex.netty.protocol.http.server.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.functions.Func1;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class NettyServiceRequestHandler implements RequestHandler<ByteBuf, ByteBuf> {

  final RequestHandlerMatcher handlerFinder;
  final ContentConverters converters;
  final ResponseWriter responseWriter;

  Logger logger = LoggerFactory.getLogger(NettyServiceRequestHandler.class);

  public NettyServiceRequestHandler(RequestHandlerMatcher handlerFinder, ContentConverters converters) {
    this.handlerFinder = handlerFinder;
    this.converters = converters;
    this.responseWriter = new ResponseWriter(converters);
  }

  @Override
  public Observable<Void> handle(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {
    try {
      String requestPath = request.getPath();
      logger.debug("Server => Request: " + requestPath);
      return request.getContent().flatMap(processRequest(request, response));
    } catch (Throwable e) {
      return respondWithServerError(request, response, e);
    }
  }

  Func1<ByteBuf, Observable<? extends Void>> processRequest(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response)  {
    return (byteBuf) -> {
      RequestHandlerMatcher.MatchingHandlers matchingHandlers = handlerFinder.getMatchingHandlers(request);

      if (matchingHandlers.getMethodHandlers().isEmpty()) {
        return respondWithMethodNotAllowed(request, response);
      } else if (matchingHandlers.getContentHandler().isPresent()) {
          return respondWithServiceResponse(request, response, byteBuf, matchingHandlers.getContentHandler().get());
      } else {
        return respondWithNotAcceptable(response);
      }
    };
  }

  private RestResponse handleService(NettyRequestWrapper requestWrapper, HandlerDefinition requestHandler) {
    RestResponse defaultResponse = new DefaultRestResponse(HttpResponseStatus.OK.code(), new HashMap<>(), false);
    RestResponse beforeResponse = transform(requestHandler.getBeforeHandlers(), requestWrapper, defaultResponse);
    if (beforeResponse.isImmediate()) {
      return beforeResponse;
    } else {
      return transform(requestHandler.getAfterHandlers(), requestWrapper, executeHandler(requestWrapper, requestHandler, beforeResponse));
    }
  }

  private RestResponse executeHandler(NettyRequestWrapper requestWrapper, HandlerDefinition requestHandler, RestResponse beforeResponse) {
   return requestHandler.getRequestHandler().handle(requestWrapper, beforeResponse);
  }

  public RestResponse transform(List<RestRequestHandler> transformers, NettyRequestWrapper requestWrapper, final RestResponse initialResponse) {
    RestResponse transformedResponse = initialResponse;
    for (RestRequestHandler transformer: transformers) {
      transformedResponse = transformer.handle(requestWrapper, transformedResponse);
      if (transformedResponse.isImmediate()) {
        return transformedResponse;
      }
    }
    return transformedResponse;
  }

  private Observable<? extends Void> respondWithServiceResponse(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response, ByteBuf bodyBytes, FoundHandlerDefinition handlerDefinition) {
    NettyRequestWrapper requestWrapper = new NettyRequestWrapper(request, bodyBytes, converters)
      .withPathParams(handlerDefinition.getMatchResult().getPathParams());

    HandlerDefinition currentHandler = handlerDefinition.getHandlerDefinition();
    RestResponse handlerResponse = handleService(requestWrapper, currentHandler);

    responseWriter.writeResponse(handlerResponse, response, currentHandler);

    return response.close(false);
  }

  private Observable<Void> respondWithServerError(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response, Throwable e) {
    logger.error("Server => Error [" + request.getPath() + "] => " + e);
    return respondWithStatusAndMessage(response, HttpResponseStatus.BAD_REQUEST, Optional.of("Error during request: \n" + StacktraceUtil.getStackTrace(e)));
  }

  private Observable<Void> respondWithMethodNotAllowed(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {
    return respondWithStatusAndMessage(response, HttpResponseStatus.METHOD_NOT_ALLOWED, Optional.of("Path Requested =>: " + request.getPath() + '\n'));
  }

  private Observable<Void> respondWithNotAcceptable(HttpServerResponse<ByteBuf> response) {
    return respondWithStatusAndMessage(response, HttpResponseStatus.NOT_ACCEPTABLE, Optional.<String>empty());
  }

  private Observable<Void> respondWithStatusAndMessage(HttpServerResponse<ByteBuf> response, HttpResponseStatus status, Optional<String> message) {
    response.setStatus(status);
    response.writeString(message.orElse(""));
    response.getHeaders().set(Header.ContentType.getName(), MediaType.TEXT_PLAIN.contentType());
    return response.close();
  }
}
