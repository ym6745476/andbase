/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http.protocol;

import java.io.IOException;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpServerConnection;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.ProtocolException;
import org.apache.http.UnsupportedHttpVersionException;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.params.HttpParams;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;

/**
 * <tt>HttpService</tt> is a server side HTTP protocol handler based on
 * the classic (blocking) I/O model.
 * <p/>
 * <tt>HttpService</tt> relies on {@link HttpProcessor} to generate mandatory
 * protocol headers for all outgoing messages and apply common, cross-cutting
 * message transformations to all incoming and outgoing messages, whereas
 * individual {@link HttpRequestHandler}s are expected to implement
 * application specific content generation and processing.
 * <p/>
 * <tt>HttpService</tt> uses {@link HttpRequestHandlerResolver} to resolve
 * matching request handler for a particular request URI of an incoming HTTP
 * request.
 * <p/>
 * <tt>HttpService</tt> can use optional {@link HttpExpectationVerifier}
 * to ensure that incoming requests meet server's expectations.
 *
 * @since 4.0
 */
@Immutable // provided injected dependencies are immutable and deprecated methods are not used
public class HttpService {

    /**
     * TODO: make all variables final in the next major version
     */
    private volatile HttpParams params = null;
    private volatile HttpProcessor processor = null;
    private volatile HttpRequestHandlerResolver handlerResolver = null;
    private volatile ConnectionReuseStrategy connStrategy = null;
    private volatile HttpResponseFactory responseFactory = null;
    private volatile HttpExpectationVerifier expectationVerifier = null;

    /**
     * Create a new HTTP service.
     *
     * @param processor            the processor to use on requests and responses
     * @param connStrategy         the connection reuse strategy
     * @param responseFactory      the response factory
     * @param handlerResolver      the handler resolver. May be null.
     * @param expectationVerifier  the expectation verifier. May be null.
     * @param params               the HTTP parameters
     *
     * @since 4.1
     */
    public HttpService(
            final HttpProcessor processor,
            final ConnectionReuseStrategy connStrategy,
            final HttpResponseFactory responseFactory,
            final HttpRequestHandlerResolver handlerResolver,
            final HttpExpectationVerifier expectationVerifier,
            final HttpParams params) {
        super();
        if (processor == null) {
            throw new IllegalArgumentException("HTTP processor may not be null");
        }
        if (connStrategy == null) {
            throw new IllegalArgumentException("Connection reuse strategy may not be null");
        }
        if (responseFactory == null) {
            throw new IllegalArgumentException("Response factory may not be null");
        }
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        this.processor = processor;
        this.connStrategy = connStrategy;
        this.responseFactory = responseFactory;
        this.handlerResolver = handlerResolver;
        this.expectationVerifier = expectationVerifier;
        this.params = params;
    }

    /**
     * Create a new HTTP service.
     *
     * @param processor            the processor to use on requests and responses
     * @param connStrategy         the connection reuse strategy
     * @param responseFactory      the response factory
     * @param handlerResolver      the handler resolver. May be null.
     * @param params               the HTTP parameters
     *
     * @since 4.1
     */
    public HttpService(
            final HttpProcessor processor,
            final ConnectionReuseStrategy connStrategy,
            final HttpResponseFactory responseFactory,
            final HttpRequestHandlerResolver handlerResolver,
            final HttpParams params) {
        this(processor, connStrategy, responseFactory, handlerResolver, null, params);
    }

    /**
     * Create a new HTTP service.
     *
     * @param proc             the processor to use on requests and responses
     * @param connStrategy     the connection reuse strategy
     * @param responseFactory  the response factory
     *
     * @deprecated (4.1) use {@link HttpService#HttpService(HttpProcessor,
     *  ConnectionReuseStrategy, HttpResponseFactory, HttpRequestHandlerResolver, HttpParams)}
     */
    @Deprecated
    public HttpService(
            final HttpProcessor proc,
            final ConnectionReuseStrategy connStrategy,
            final HttpResponseFactory responseFactory) {
        super();
        setHttpProcessor(proc);
        setConnReuseStrategy(connStrategy);
        setResponseFactory(responseFactory);
    }

    /**
     * @deprecated (4.1) set {@link HttpProcessor} using constructor
     */
    @Deprecated
    public void setHttpProcessor(final HttpProcessor processor) {
        if (processor == null) {
            throw new IllegalArgumentException("HTTP processor may not be null");
        }
        this.processor = processor;
    }

    /**
     * @deprecated (4.1) set {@link ConnectionReuseStrategy} using constructor
     */
    @Deprecated
    public void setConnReuseStrategy(final ConnectionReuseStrategy connStrategy) {
        if (connStrategy == null) {
            throw new IllegalArgumentException("Connection reuse strategy may not be null");
        }
        this.connStrategy = connStrategy;
    }

    /**
     * @deprecated (4.1) set {@link HttpResponseFactory} using constructor
     */
    @Deprecated
    public void setResponseFactory(final HttpResponseFactory responseFactory) {
        if (responseFactory == null) {
            throw new IllegalArgumentException("Response factory may not be null");
        }
        this.responseFactory = responseFactory;
    }

    /**
     * @deprecated (4.1) set {@link HttpResponseFactory} using constructor
     */
    @Deprecated
    public void setParams(final HttpParams params) {
        this.params = params;
    }

    /**
     * @deprecated (4.1) set {@link HttpRequestHandlerResolver} using constructor
     */
    @Deprecated
    public void setHandlerResolver(final HttpRequestHandlerResolver handlerResolver) {
        this.handlerResolver = handlerResolver;
    }

    /**
     * @deprecated (4.1) set {@link HttpExpectationVerifier} using constructor
     */
    @Deprecated
    public void setExpectationVerifier(final HttpExpectationVerifier expectationVerifier) {
        this.expectationVerifier = expectationVerifier;
    }

    public HttpParams getParams() {
        return this.params;
    }

    /**
     * Handles receives one HTTP request over the given connection within the
     * given execution context and sends a response back to the client.
     *
     * @param conn the active connection to the client
     * @param context the actual execution context.
     * @throws IOException in case of an I/O error.
     * @throws HttpException in case of HTTP protocol violation or a processing
     *   problem.
     */
    public void handleRequest(
            final HttpServerConnection conn,
            final HttpContext context) throws IOException, HttpException {

        context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);

        HttpResponse response = null;

        try {

            HttpRequest request = conn.receiveRequestHeader();
            request.setParams(
                    new DefaultedHttpParams(request.getParams(), this.params));

            if (request instanceof HttpEntityEnclosingRequest) {

                if (((HttpEntityEnclosingRequest) request).expectContinue()) {
                    response = this.responseFactory.newHttpResponse(HttpVersion.HTTP_1_1,
                            HttpStatus.SC_CONTINUE, context);
                    response.setParams(
                            new DefaultedHttpParams(response.getParams(), this.params));

                    if (this.expectationVerifier != null) {
                        try {
                            this.expectationVerifier.verify(request, response, context);
                        } catch (HttpException ex) {
                            response = this.responseFactory.newHttpResponse(HttpVersion.HTTP_1_0,
                                    HttpStatus.SC_INTERNAL_SERVER_ERROR, context);
                            response.setParams(
                                    new DefaultedHttpParams(response.getParams(), this.params));
                            handleException(ex, response);
                        }
                    }
                    if (response.getStatusLine().getStatusCode() < 200) {
                        // Send 1xx response indicating the server expections
                        // have been met
                        conn.sendResponseHeader(response);
                        conn.flush();
                        response = null;
                        conn.receiveRequestEntity((HttpEntityEnclosingRequest) request);
                    }
                } else {
                    conn.receiveRequestEntity((HttpEntityEnclosingRequest) request);
                }
            }

            context.setAttribute(ExecutionContext.HTTP_REQUEST, request);

            if (response == null) {
                response = this.responseFactory.newHttpResponse(HttpVersion.HTTP_1_1,
                        HttpStatus.SC_OK, context);
                response.setParams(
                        new DefaultedHttpParams(response.getParams(), this.params));
                this.processor.process(request, context);
                doService(request, response, context);
            }

            // Make sure the request content is fully consumed
            if (request instanceof HttpEntityEnclosingRequest) {
                HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
                EntityUtils.consume(entity);
            }

        } catch (HttpException ex) {
            response = this.responseFactory.newHttpResponse
                (HttpVersion.HTTP_1_0, HttpStatus.SC_INTERNAL_SERVER_ERROR,
                 context);
            response.setParams(
                    new DefaultedHttpParams(response.getParams(), this.params));
            handleException(ex, response);
        }

        context.setAttribute(ExecutionContext.HTTP_RESPONSE, response);

        this.processor.process(response, context);
        conn.sendResponseHeader(response);
        conn.sendResponseEntity(response);
        conn.flush();

        if (!this.connStrategy.keepAlive(response, context)) {
            conn.close();
        }
    }

    /**
     * Handles the given exception and generates an HTTP response to be sent
     * back to the client to inform about the exceptional condition encountered
     * in the course of the request processing.
     *
     * @param ex the exception.
     * @param response the HTTP response.
     */
    protected void handleException(final HttpException ex, final HttpResponse response) {
        if (ex instanceof MethodNotSupportedException) {
            response.setStatusCode(HttpStatus.SC_NOT_IMPLEMENTED);
        } else if (ex instanceof UnsupportedHttpVersionException) {
            response.setStatusCode(HttpStatus.SC_HTTP_VERSION_NOT_SUPPORTED);
        } else if (ex instanceof ProtocolException) {
            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        } else {
            response.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
        String message = ex.getMessage();
        if (message == null) {
            message = ex.toString();
        }
        byte[] msg = EncodingUtils.getAsciiBytes(message);
        ByteArrayEntity entity = new ByteArrayEntity(msg);
        entity.setContentType("text/plain; charset=US-ASCII");
        response.setEntity(entity);
    }

    /**
     * The default implementation of this method attempts to resolve an
     * {@link HttpRequestHandler} for the request URI of the given request
     * and, if found, executes its
     * {@link HttpRequestHandler#handle(HttpRequest, HttpResponse, HttpContext)}
     * method.
     * <p>
     * Super-classes can override this method in order to provide a custom
     * implementation of the request processing logic.
     *
     * @param request the HTTP request.
     * @param response the HTTP response.
     * @param context the execution context.
     * @throws IOException in case of an I/O error.
     * @throws HttpException in case of HTTP protocol violation or a processing
     *   problem.
     */
    protected void doService(
            final HttpRequest request,
            final HttpResponse response,
            final HttpContext context) throws HttpException, IOException {
        HttpRequestHandler handler = null;
        if (this.handlerResolver != null) {
            String requestURI = request.getRequestLine().getUri();
            handler = this.handlerResolver.lookup(requestURI);
        }
        if (handler != null) {
            handler.handle(request, response, context);
        } else {
            response.setStatusCode(HttpStatus.SC_NOT_IMPLEMENTED);
        }
    }

}
