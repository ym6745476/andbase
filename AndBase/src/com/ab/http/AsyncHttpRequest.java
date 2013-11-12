/*
 * Copyright (C) 2013 www.418log.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.ab.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

// TODO: Auto-generated Javadoc
/**
 * The Class AsyncHttpRequest.
 */
class AsyncHttpRequest implements Runnable {
    
    /** The client. */
    private final AbstractHttpClient client;
    
    /** The context. */
    private final HttpContext context;
    
    /** The request. */
    private final HttpUriRequest request;
    
    /** The response handler. */
    private final AsyncHttpResponseHandler responseHandler;
    
    /** The execution count. */
    private int executionCount;

    /**
     * Instantiates a new async http request.
     *
     * @param client the client
     * @param context the context
     * @param request the request
     * @param responseHandler the response handler
     */
    public AsyncHttpRequest(AbstractHttpClient client, HttpContext context, HttpUriRequest request, AsyncHttpResponseHandler responseHandler) {
        this.client = client;
        this.context = context;
        this.request = request;
        this.responseHandler = responseHandler;
    }

    /**
     * 描述：TODO
     * @see java.lang.Runnable#run()
     * @author: zhaoqp
     * @date：2013-10-22 下午4:23:14
     * @version v1.0
     */
    @Override
    public void run() {
        if (responseHandler != null) {
            responseHandler.sendStartMessage();
        }

        try {
            makeRequestWithRetries();
        } catch (IOException e) {
            if (responseHandler != null) {
                responseHandler.sendFailureMessage(0, null, null, e);
            }
        }
        
        if (responseHandler != null) {
            responseHandler.sendFinishMessage();
        }
    }

    /**
     * Make request.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void makeRequest() throws IOException {
        if (!Thread.currentThread().isInterrupted()) {
            // Fixes #115
            if (request.getURI().getScheme() == null) {
                // subclass of IOException so processed in the caller
                throw new MalformedURLException("No valid URI scheme was provided");
            }

            HttpResponse response = client.execute(request, context);

            if (!Thread.currentThread().isInterrupted()) {
                if (responseHandler != null) {
                    responseHandler.sendResponseMessage(response);
                }
            }
        }
    }

    /**
     * Make request with retries.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void makeRequestWithRetries() throws IOException {
        boolean retry = true;
        IOException cause = null;
        HttpRequestRetryHandler retryHandler = client.getHttpRequestRetryHandler();
        try
        {
            while (retry) {
                try {
                    makeRequest();
                    return;
                } catch (UnknownHostException e) {
                    // switching between WI-FI and mobile data networks can cause a retry which then results in an UnknownHostException
                    // while the WI-FI is initialising. The retry logic will be invoked here, if this is NOT the first retry
                    // (to assist in genuine cases of unknown host) which seems better than outright failure
                    cause = new IOException("UnknownHostException exception: " + e.getMessage());
                    retry = (executionCount > 0) && retryHandler.retryRequest(cause, ++executionCount, context);
                } catch (NullPointerException e) {
                    // there's a bug in HttpClient 4.0.x that on some occasions causes
                    // DefaultRequestExecutor to throw an NPE, see
                    // http://code.google.com/p/android/issues/detail?id=5255
                    cause = new IOException("NPE in HttpClient: " + e.getMessage());
                    retry = retryHandler.retryRequest(cause, ++executionCount, context);
                } catch (IOException e) {
                    cause = e;
                    retry = retryHandler.retryRequest(cause, ++executionCount, context);
                }
                if(retry && (responseHandler != null)) {
                    responseHandler.sendRetryMessage();
                }
            }
        } catch (Exception e) {
            // catch anything else to ensure failure message is propagated
            cause = new IOException("Unhandled exception: " + e.getMessage());
        }
        
        // cleaned up to throw IOException
        throw(cause);
    }
}
