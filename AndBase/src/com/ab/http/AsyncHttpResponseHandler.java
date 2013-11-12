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

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.util.ByteArrayBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;

// TODO: Auto-generated Javadoc
/**
 * Used to intercept and handle the responses from requests made using.
 *
 * {@link AsyncHttpClient}. The {@link #onSuccess(int, org.apache.http.Header[], byte[])} method is
 * designed to be anonymously overridden with your own response handling code.
 * <p>&nbsp;</p>
 * Additionally, you can override the {@link #onFailure(int, org.apache.http.Header[], byte[], Throwable)},
 * {@link #onStart()}, {@link #onFinish()}, {@link #onRetry()} and {@link #onProgress(int, int)} methods as required.
 * <p>&nbsp;</p>
 * For example:
 * <p>&nbsp;</p>
 * <pre>
 * AsyncHttpClient client = new AsyncHttpClient();
 * client.get("http://www.google.com", new AsyncHttpResponseHandler() {
 * &#064;Override
 * public void onStart() {
 * // Initiated the request
 * }
 * 
 * &#064;Override
 * public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
 * // Successfully got a response
 * }
 * 
 * &#064;Override
 * public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
 * // Response failed :(
 * }
 * 
 * &#064;Override
 * public void onRetry() {
 * // Request was retried
 * }
 * 
 * &#064;Override
 * public void onProgress(int bytesWritten, int totalSize) {
 * // Progress notification
 * }
 * 
 * &#064;Override
 * public void onFinish() {
 * // Completed the request (either success or failure)
 * }
 * });
 * </pre>
 */
public class AsyncHttpResponseHandler {
    
    /** The Constant LOG_TAG. */
    private static final String LOG_TAG = "AsyncHttpResponseHandler";

    /** The Constant SUCCESS_MESSAGE. */
    protected static final int SUCCESS_MESSAGE = 0;
    
    /** The Constant FAILURE_MESSAGE. */
    protected static final int FAILURE_MESSAGE = 1;
    
    /** The Constant START_MESSAGE. */
    protected static final int START_MESSAGE = 2;
    
    /** The Constant FINISH_MESSAGE. */
    protected static final int FINISH_MESSAGE = 3;
    
    /** The Constant PROGRESS_MESSAGE. */
    protected static final int PROGRESS_MESSAGE = 4;
    
    /** The Constant RETRY_MESSAGE. */
    protected static final int RETRY_MESSAGE = 5;

    /** The Constant BUFFER_SIZE. */
    protected static final int BUFFER_SIZE = 4096;

    /** The handler. */
    private Handler handler;
    
    /** The Constant DEFAULT_CHARSET. */
    public static final String DEFAULT_CHARSET = "UTF-8";
    
    /** The response charset. */
    private String responseCharset = DEFAULT_CHARSET;
    
    /** The use synchronous mode. */
    private Boolean useSynchronousMode = false;

    // avoid leaks by using a non-anonymous handler class
    // with a weak reference
    /**
     * The Class ResponderHandler.
     */
    static class ResponderHandler extends Handler {
        
        /** The m responder. */
        private final WeakReference<AsyncHttpResponseHandler> mResponder;

        /**
         * Instantiates a new responder handler.
         *
         * @param service the service
         */
        ResponderHandler(AsyncHttpResponseHandler service) {
            mResponder = new WeakReference<AsyncHttpResponseHandler>(service);
        }

        /**
         * 描述：TODO
         * @see android.os.Handler#handleMessage(android.os.Message)
         * @author: zhaoqp
         * @date：2013-10-22 下午4:23:15
         * @version v1.0
         */
        @Override
        public void handleMessage(Message msg) {
            AsyncHttpResponseHandler service = mResponder.get();
            if (service != null) {
                service.handleMessage(msg);
            }
        }
    }

    /**
     * Gets the use synchronous mode.
     *
     * @return the use synchronous mode
     */
    public boolean getUseSynchronousMode() {
        return (useSynchronousMode);
    }

    /**
     * Set the response handler to use synchronous mode or not.
     *
     * @param value true indicates that synchronous mode should be used
     */
    public void setUseSynchronousMode(Boolean value) {
        useSynchronousMode = value;
    }

    /**
     * Sets the charset for the response string. If not set, the default is UTF-8.
     *
     * @param charset to be used for the response string.
     * @see <a href="http://docs.oracle.com/javase/7/docs/api/java/nio/charset/Charset.html">Charset</a>
     */
    public void setCharset(final String charset) {
        this.responseCharset = charset;
    }

    /**
     * Gets the charset.
     *
     * @return the charset
     */
    public String getCharset() {
        return this.responseCharset == null ? DEFAULT_CHARSET : this.responseCharset;
    }

    /**
     * Creates a new AsyncHttpResponseHandler.
     */
    public AsyncHttpResponseHandler() {
        // Set up a handler to post events back to the correct thread if possible
        if (Looper.myLooper() != null) {
            handler = new ResponderHandler(this);
        }
    }


    //
    // Callbacks to be overridden, typically anonymously
    //

    /**
     * Fired when the request progress, override to handle in your own code.
     *
     * @param bytesWritten offset from start of file
     * @param totalSize    total size of file
     */
    public void onProgress(int bytesWritten, int totalSize) {
    }

    /**
     * Fired when the request is started, override to handle in your own code.
     */
    public void onStart() {
    }

    /**
     * Fired in all cases when the request is finished, after both success and failure, override to handle in your own code.
     */
    public void onFinish() {
    }

    /**
     * Fired when a request returns successfully, override to handle in your own code.
     *
     * @param content the body of the HTTP response from the server
     * @deprecated use {@link #onSuccess(int, Header[], byte[])}
     */
    @Deprecated
    public void onSuccess(String content) {
    }

    /**
     * Fired when a request returns successfully, override to handle in your own code.
     *
     * @param statusCode the status code of the response
     * @param headers    the headers of the HTTP response
     * @param content    the body of the HTTP response from the server
     * @deprecated use {@link #onSuccess(int, Header[], byte[])}
     */
    @Deprecated
    public void onSuccess(int statusCode, Header[] headers, String content) {
        onSuccess(statusCode, content);
    }

    /**
     * Fired when a request returns successfully, override to handle in your own code.
     *
     * @param statusCode the status code of the response
     * @param content    the body of the HTTP response from the server
     * @deprecated use {@link #onSuccess(int, Header[], byte[])}
     */
    @Deprecated
    public void onSuccess(int statusCode, String content) {
        onSuccess(content);
    }

    /**
     * Fired when a request returns successfully, override to handle in your own code.
     *
     * @param statusCode   the status code of the response
     * @param headers      return headers, if any
     * @param responseBody the body of the HTTP response from the server
     */
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        try {
            String response = responseBody == null ? null : new String(responseBody, getCharset());
            onSuccess(statusCode, headers, response);
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG_TAG, e.toString());
            onFailure(statusCode, headers, e, null);
        }
    }

    /**
     * Fired when a request fails to complete, override to handle in your own code.
     *
     * @param error the underlying cause of the failure
     * @deprecated use {@link #onFailure(Throwable, String)}
     */
    @Deprecated
    public void onFailure(Throwable error) {
    }

    /**
     * Fired when a request fails to complete, override to handle in your own code.
     *
     * @param error   the underlying cause of the failure
     * @param content the response body, if any
     * @deprecated use {@link #onFailure(int, Header[], byte[], Throwable)}
     */
    @Deprecated
    public void onFailure(Throwable error, String content) {
        // By default, call the deprecated onFailure(Throwable) for compatibility
        onFailure(error);
    }

    /**
     * Fired when a request fails to complete, override to handle in your own code.
     *
     * @param statusCode return HTTP status code
     * @param error      the underlying cause of the failure
     * @param content    the response body, if any
     * @deprecated use {@link #onFailure(int, Header[], byte[], Throwable)}
     */
    @Deprecated
    public void onFailure(int statusCode, Throwable error, String content) {
        // By default, call the chain method onFailure(Throwable,String)
        onFailure(error, content);
    }

    /**
     * Fired when a request fails to complete, override to handle in your own code.
     *
     * @param statusCode return HTTP status code
     * @param headers    return headers, if any
     * @param error      the underlying cause of the failure
     * @param content    the response body, if any
     * @deprecated use {@link #onFailure(int, Header[], byte[], Throwable)}
     */
    @Deprecated
    public void onFailure(int statusCode, Header[] headers, Throwable error, String content) {
        // By default, call the chain method onFailure(int,Throwable,String)
        onFailure(statusCode, error, content);
    }

    /**
     * Fired when a request fails to complete, override to handle in your own code.
     *
     * @param statusCode   return HTTP status code
     * @param headers      return headers, if any
     * @param responseBody the response body, if any
     * @param error        the underlying cause of the failure
     */
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        String response = null;
        try {
        	if(responseBody!=null){
        		response = new String(responseBody, getCharset());
        	}
            onFailure(statusCode, headers, error, response);
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG_TAG, e.toString());
            onFailure(statusCode, headers, e, null);
        }
    }

    /**
     * Fired when a retry occurs, override to handle in your own code.
     */
    public void onRetry() {
    }


    //
    // Pre-processing of messages (executes in background threadpool thread)
    //

    /**
     * Send progress message.
     *
     * @param bytesWritten the bytes written
     * @param totalSize the total size
     */
    public void sendProgressMessage(int bytesWritten, int totalSize) {
        sendMessage(obtainMessage(PROGRESS_MESSAGE, new Object[]{bytesWritten, totalSize}));
    }

    /**
     * Send success message.
     *
     * @param statusCode the status code
     * @param headers the headers
     * @param responseBody the response body
     */
    public void sendSuccessMessage(int statusCode, Header[] headers, byte[] responseBody) {
        sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[]{statusCode, headers, responseBody}));
    }

    /**
     * Send failure message.
     *
     * @param statusCode the status code
     * @param headers the headers
     * @param responseBody the response body
     * @param error the error
     */
    public void sendFailureMessage(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[]{statusCode, headers, responseBody, error}));
    }

    /**
     * Send start message.
     */
    public void sendStartMessage() {
        sendMessage(obtainMessage(START_MESSAGE, null));
    }

    /**
     * Send finish message.
     */
    public void sendFinishMessage() {
        sendMessage(obtainMessage(FINISH_MESSAGE, null));
    }

    /**
     * Send retry message.
     */
    public void sendRetryMessage() {
        sendMessage(obtainMessage(RETRY_MESSAGE, null));
    }

    // Methods which emulate android's Handler and Message methods
    /**
     * Handle message.
     *
     * @param msg the msg
     */
    private void handleMessage(Message msg) {
        Object[] response;

        switch (msg.what) {
            case SUCCESS_MESSAGE:
                response = (Object[]) msg.obj;
                if (response != null && response.length >= 3)
                    onSuccess((Integer) response[0], (Header[]) response[1], (byte[]) response[2]);
                else
                    Log.e(LOG_TAG, "SUCCESS_MESSAGE didn't got enough params");
                break;
            case FAILURE_MESSAGE:
                response = (Object[]) msg.obj;
                if (response != null && response.length >= 4)
                    onFailure((Integer) response[0], (Header[]) response[1], (byte[]) response[2], (Throwable) response[3]);
                else
                    Log.e(LOG_TAG, "FAILURE_MESSAGE didn't got enough params");
                break;
            case START_MESSAGE:
                onStart();
                break;
            case FINISH_MESSAGE:
                onFinish();
                break;
            case PROGRESS_MESSAGE:
                response = (Object[]) msg.obj;
                if (response != null && response.length >= 2)
                    onProgress((Integer) response[0], (Integer) response[1]);
                else
                    Log.e(LOG_TAG, "PROGRESS_MESSAGE didn't got enough params");
                break;
            case RETRY_MESSAGE:
                onRetry();
                break;
        }
    }

    /**
     * Send message.
     *
     * @param msg the msg
     */
    public void sendMessage(Message msg) {
        if (getUseSynchronousMode() || handler == null) {
            handleMessage(msg);
        } else if (!Thread.currentThread().isInterrupted()) { // do not send messages if request has been cancelled
            handler.sendMessage(msg);
        }
    }

    /**
     * Post runnable.
     *
     * @param r the r
     */
    protected void postRunnable(Runnable r) {
        if (r != null) {
            handler.post(r);
        }
    }

    /**
     * Obtain message.
     *
     * @param responseMessage the response message
     * @param response the response
     * @return the message
     */
    protected Message obtainMessage(int responseMessage, Object response) {
        Message msg;
        if (handler != null) {
            msg = handler.obtainMessage(responseMessage, response);
        } else {
            msg = Message.obtain();
            if (msg != null) {
                msg.what = responseMessage;
                msg.obj = response;
            }
        }
        return msg;
    }

    // Interface to AsyncHttpRequest
    /**
     * Send response message.
     *
     * @param response the response
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void sendResponseMessage(HttpResponse response) throws IOException {
        // do not process if request has been cancelled
        if (!Thread.currentThread().isInterrupted()) {
            StatusLine status = response.getStatusLine();
            byte[] responseBody;
            responseBody = getResponseData(response.getEntity());
            // additional cancellation check as getResponseData() can take non-zero time to process
            if (!Thread.currentThread().isInterrupted()) {
                if (status.getStatusCode() >= 300) {
                    sendFailureMessage(status.getStatusCode(), response.getAllHeaders(), responseBody, new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()));
                } else {
                    sendSuccessMessage(status.getStatusCode(), response.getAllHeaders(), responseBody);
                }
            }
        }
    }

    /**
     * Gets the response data.
     *
     * @param entity the entity
     * @return the response data
     * @throws IOException Signals that an I/O exception has occurred.
     */
    byte[] getResponseData(HttpEntity entity) throws IOException {
        byte[] responseBody = null;
        if (entity != null) {
            InputStream instream = entity.getContent();
            if (instream != null) {
                long contentLength = entity.getContentLength();
                if (contentLength > Integer.MAX_VALUE) {
                    throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
                }
                if (contentLength < 0) {
                    contentLength = BUFFER_SIZE;
                }
                try {
                    ByteArrayBuffer buffer = new ByteArrayBuffer((int) contentLength);
                    try {
                        byte[] tmp = new byte[BUFFER_SIZE];
                        int l, count = 0;
                        // do not send messages if request has been cancelled
                        while ((l = instream.read(tmp)) != -1 && !Thread.currentThread().isInterrupted()) {
                            count += l;
                            buffer.append(tmp, 0, l);
                            sendProgressMessage(count, (int) contentLength);
                        }
                    } finally {
                        instream.close();
                    }
                    responseBody = buffer.buffer();
                } catch (OutOfMemoryError e) {
                    System.gc();
                    throw new IOException("File too large to fit into available memory");
                }
            }
        }
        return responseBody;
    }
}
