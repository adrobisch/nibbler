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
import java.util.Optional;

public class NettyServiceRequestHandler implements RequestHandler<ByteBuf, ByteBuf> {

  final RequestHandlerMatcher handlerFinder;
  final ContentConverters converters;
  final NettyResponseWriter responseWriter;

  Logger logger = LoggerFactory.getLogger(NettyServiceRequestHandler.class);

  public NettyServiceRequestHandler(RequestHandlerMatcher handlerFinder, ContentConverters converters) {
    this.handlerFinder = handlerFinder;
    this.converters = converters;
    this.responseWriter = new NettyResponseWriter(converters);
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

  Func1<ByteBuf, Observable<? extends Void>> processRequest(HttpServerRequest<ByteBuf> nettyRequest, HttpServerResponse<ByteBuf> response)  {
    return (byteBuf) -> {
      NettyRequestWrapper request = new NettyRequestWrapper(nettyRequest, byteBuf, converters);
      RequestHandlerMatcher.MatchingHandlers matchingHandlers = handlerFinder.getMatchingHandlers(request);
      if (matchingHandlers.getContentHandler().isPresent()) {
        return respondWithServiceResponse(request, response, matchingHandlers.getContentHandler().get());
      } else if (matchingHandlers.getMatchingPathHandlers().isEmpty()) {
        return respondWithNotFound(nettyRequest, response);
      } else if (matchingHandlers.getMethodHandlers().isEmpty()) {
        return respondWithMethodNotAllowed(nettyRequest, response);
      } else {
        return respondWithNotAcceptable(response);
      }
    };
  }

  private RestResponse handleService(RestRequest request, HandlerDefinition requestHandler) {
    RestResponse initialResponse = new DefaultRestResponse(HttpResponseStatus.OK.code(), new HashMap<>(), false);
    return requestHandler.getRequestHandler().handle(request, initialResponse);
  }

  private Observable<? extends Void> respondWithServiceResponse(NettyRequestWrapper request, HttpServerResponse<ByteBuf> response, FoundHandlerDefinition handlerDefinition) {
    HandlerDefinition currentHandler = handlerDefinition.getHandlerDefinition();
    request.withPathParams(handlerDefinition.getMatchResult().getPathParams());

    RestResponse handlerResponse = handleService(request, currentHandler);
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

  private Observable<Void> respondWithNotFound(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {
    return respondWithStatusAndMessage(response, HttpResponseStatus.NOT_FOUND, Optional.of("Path Requested =>: " + request.getPath() + '\n'));
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
